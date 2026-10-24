package cl.duoc.reservaespacios.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application) : AndroidViewModel(application) {
    val reservas = mutableStateListOf<Reserva>()

    private val db = AppDatabase.getInstance(application)
    private val reservaRepository = ReservaRepository(db.reservaDao())
    private val espacioRepository = EspacioRepository(db.espacioDao())
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

    //nuevo
    fun existeUsuario(correo: String): Boolean = usuarios.any { it.correo == correo }

    fun passwordCorrecta(correo: String, pass: String): Boolean =
        usuarios.any { it.correo == correo && it.password == pass }

    fun iniciarSesion(correo: String) {
        correoUsuario = correo
        sesionRepository.guardarCorreo(correo)
    }

    fun cerrarSesion() {
        correoUsuario = ""
        sesionRepository.cerrar()
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

    fun cancelar(reserva: Reserva) = actualizar(reserva.copy(estado = "Cancelada"))

    fun aprobar(reserva: Reserva) = actualizar(reserva.copy(estado = "Aprobada"))

    fun rechazar(reserva: Reserva) = actualizar(reserva.copy(estado = "Rechazada"))

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
}