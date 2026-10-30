package cl.duoc.reservaespacios.ui.viewmodel

import android.R
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.reservaespacios.data.repository.EspacioRepository
import cl.duoc.reservaespacios.data.repository.ReservaRepository
import cl.duoc.reservaespacios.data.repository.SesionRepository
import cl.duoc.reservaespacios.data.repository.UsuarioRepository
import cl.duoc.reservaespacios.data.room.AppDatabase
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.Usuario
import cl.duoc.reservaespacios.utils.fechaDesdeHoy
import cl.duoc.reservaespacios.utils.horaActual
import cl.duoc.reservaespacios.utils.numeroFecha
import cl.duoc.reservaespacios.utils.numeroHora
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application) : AndroidViewModel(application) {

    val reservas = mutableStateListOf<Reserva>()

    private val db = AppDatabase.getInstance(application)
    private val reservaRepository = ReservaRepository(db.reservaDao())
    private val espacioRepository = EspacioRepository(db.espacioDao(), application.filesDir)
    private val usuarioRepository = UsuarioRepository(db.usuarioDao())
    private val sesionRepository = SesionRepository(application)

    var correoUsuario by mutableStateOf(sesionRepository.obtenerCorreo())
        private set

    val usuarios = mutableStateListOf<Usuario>()

    val espacios = mutableStateListOf<Espacio>()

    var pestanaActual by mutableStateOf("Inicio")

    val usuarioActual: Usuario?
        get() = usuarios.find {
            it.correo.equals(
                correoUsuario,
                ignoreCase = true
            )
        }

    val nombreUsuario: String get() = usuarioActual?.nombre ?: ""

    val proximaReserva : Reserva? get() {
        val hoy = numeroFecha(fechaDesdeHoy(0))
        return reservas.filter{
            it.correoUsuario.equals(correoUsuario, ignoreCase = true) &&
                    it.estado == "Aprobada" && numeroFecha(it.fecha) >= hoy
        }.minByOrNull { numeroFecha(it.fecha).toLong() * 10000 + numeroHora(it.horario) }
    }

    val misReservas : List<Reserva> get() = reservas.filter { it.correoUsuario.equals(correoUsuario, ignoreCase = true) }

    val inicialesUsuario: String
        get() {
            val usuario = usuarioActual
            return if (usuario != null) {
                (usuario.nombre.take(1) + usuario.apellido.take(1)).uppercase()
            } else {
                correoUsuario.substringBefore("@").split(".").take(2)
                    .joinToString("") { it.take(1).uppercase() }
            }
        }

    val rolUsuario: String get() = usuarioActual?.rol ?: ""

    val esAdmin: Boolean get() = rolUsuario == "Administrador"

    fun iniciarSesion(correo: String) {
        correoUsuario = correo
        sesionRepository.guardarCorreo(correo)
    }

    fun cerrarSesion() {
        correoUsuario = ""
        pestanaActual = "Inicio"
        sesionRepository.cerrar()
    }

    // Funcion para la Foto de Perfil
    fun actualizarFotoUsuario(nuevaFoto: String) {
        val usuario = usuarioActual
        if (usuario != null) {
            val index = usuarios.indexOfFirst { it.correo.equals(usuario.correo, ignoreCase = true) }
            if (index != -1) {
                val usuarioActualizado = usuario.copy(foto = nuevaFoto)
                usuarios[index] = usuarioActualizado
                viewModelScope.launch {
                    usuarioRepository.actualizarUsuario(usuarioActualizado)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            reservas.addAll(reservaRepository.cargarReservas())
            espacios.addAll(espacioRepository.cargarEspacios())
            usuarios.addAll(usuarioRepository.cargarUsuarios())
        }
    }

    private fun actualizar(reservaCambiada: Reserva) {
        val index = reservas.indexOfFirst { it.id == reservaCambiada.id }
        reservas[index] = reservaCambiada
        viewModelScope.launch { reservaRepository.actualizar(reservaCambiada) }
    }

    // Notificaciones del sistema sobre si fue aprobado o no una reserva
    private fun mostrarNotificacionSistema(titulo: String, mensaje: String) {
        val context = getApplication<Application>()
        val channelId = "reservas_channel_id"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Reservas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de aprobación y rechazo de reservas"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (_: SecurityException) {
        }
    }

    fun cancelar(reserva: Reserva) = actualizar(reserva.copy(estado = "Cancelada"))

    fun aprobar(reserva: Reserva) { actualizar(reserva.copy(estado = "Aprobada"))
        mostrarNotificacionSistema(
            "Reserva Aprobada",
            "Tu reserva para ${reserva.espacio} el ${reserva.fecha} ha sido aprobada."
        )
    }

    fun rechazar(reserva: Reserva) { actualizar(reserva.copy(estado = "Rechazada"))
        mostrarNotificacionSistema(
            "Reserva Rechazada",
            "Tu reserva para ${reserva.espacio} el ${reserva.fecha} ha sido rechazada."
        )
    }

    fun registrarIngreso(reserva: Reserva) = actualizar(reserva.copy(estado = "En uso",
        horaIngreso = horaActual())
    )

    fun registrarSalida(reserva : Reserva) = actualizar(reserva.copy(estado = "Finalizada"))

    fun agregarReserva(
        espacio: String,
        fecha: String,
        horario: String,
        motivo: String
    ) {
        val nuevoId = (reservas.maxOfOrNull { it.id } ?: 0) + 1
        val nueva = Reserva(
            nuevoId,
            espacio,
            fecha,
            horario,
            motivo,
            "Pendiente",
            correoUsuario = correoUsuario
        )
        reservas.add(nueva)
        viewModelScope.launch { reservaRepository.agregar(nueva) }
    }

    fun usuarioPorCorreo(correo: String): Usuario? =
        usuarios.find { it.correo.equals(correo, ignoreCase = true) }

    fun listaDeEspera(reserva: Reserva): Int = reservas.count {
        it.id < reserva.id && it.estado == "Pendiente" &&
                it.espacio == reserva.espacio && it.fecha == reserva.fecha && it.horario == reserva.horario
    }

    fun existeEspacio(nombre: String): Boolean =
        espacios.any { it.nombre.equals(nombre.trim(), ignoreCase = true) }

    fun agregarEspacio(
        nombre: String,
        capacidad: Int,
        equipamiento: String,
        ubicacion: String,
        estado: String,
        foto: Bitmap?
    ) {
        viewModelScope.launch {
            val rutaFoto = if (foto != null) espacioRepository.guardarFoto(nombre.trim(), foto) else ""
            val nuevo = Espacio(nombre.trim(), capacidad, equipamiento.trim(), ubicacion.trim(), estado, rutaFoto)
            espacioRepository.agregar(nuevo)
            espacios.add(nuevo)
        }
    }

    // Retorna las notificaciones procesadas solo para el usuario actual
    val notificacionesUsuario: List<Notificacion>
        get() = reservas
            // Filtramos solo las reservas del usuario actual que ya fueron respondidas
            .filter { it.correoUsuario == correoUsuario && (it.estado == "Aprobada" || it.estado == "Rechazada") }
            // Las transformamos en objetos Notificacion
            .map { reserva ->
                val titulo = if (reserva.estado == "Aprobada") "Reserva Aprobada" else "Reserva Rechazada"
                val mensaje = "Su reserva para ${reserva.espacio} (${reserva.horario}) ha sido ${reserva.estado.lowercase()}."
                Notificacion(
                    titulo = titulo,
                    mensaje = mensaje,
                    fecha = reserva.fecha,
                    leida = true
                )
            }
            .reversed()
}

// Estructura de Notificacion ocupada en notificacionesUsuario
data class Notificacion(
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val leida: Boolean
){

}