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
import cl.duoc.reservaespacios.data.room.AppDatabase
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.Usuario
import cl.duoc.reservaespacios.model.usuariosDePrueba
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application) : AndroidViewModel(application) {
    val reservas = mutableStateListOf<Reserva>()

    private val db = AppDatabase.getInstance(application)
    private val reservaRepository = ReservaRepository(db.reservaDao())
    private val espacioRepository = EspacioRepository(db.espacioDao())

    val espacios = mutableStateListOf<Espacio>()

    var pestanaActual by mutableStateOf("Inicio")

    var correoUsuario by mutableStateOf("")

    val usuarioActual: Usuario?
        get() = usuariosDePrueba.find {
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

    init {

        viewModelScope.launch {
            reservas.addAll(reservaRepository.cargarReservas())
            espacios.addAll(espacioRepository.cargarEspacios())

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