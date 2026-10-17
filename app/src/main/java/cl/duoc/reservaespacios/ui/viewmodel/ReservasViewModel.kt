package cl.duoc.reservaespacios.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.reservaespacios.data.DataStoreManager
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.Usuario
import cl.duoc.reservaespacios.model.reservasDePrueba
import cl.duoc.reservaespacios.model.usuariosDePrueba
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application): AndroidViewModel(application) {
    val reservas = mutableStateListOf<Reserva>()
    private val dataStore = DataStoreManager(application)
    var pestanaActual by mutableStateOf("Inicio")

    var correoUsuario by mutableStateOf("")

    val usuarioActual : Usuario? get() = usuariosDePrueba.find { it.correo.equals(correoUsuario, ignoreCase = true)}

    val nombreUsuario: String get() = usuarioActual?.nombre ?: ""

    val inicialesUsuario : String get() {
        val usuario = usuarioActual
        return if (usuario != null){
            (usuario.nombre.take(1) + usuario.apellido.take(1)).uppercase()
        }
        else{
            correoUsuario.substringBefore("@").split(".").take(2).joinToString("") { it.take(1).uppercase()}
        }
    }

    val rolUsuario: String get() = if(correoUsuario.lowercase().endsWith("@profesor.duoc.cl")) "Docente" else "Administrativo"

    init {

        viewModelScope.launch{
            val guardadas = dataStore.cargarReservas()
            reservas.addAll(guardadas ?: reservasDePrueba)
        }
    }

    private fun guardar(){
        viewModelScope.launch { dataStore.guardarReservas(reservas.toList()) }
    }

    fun cancelar(reserva: Reserva) {
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Cancelada")
        guardar()
    }

    fun aprobar(reserva: Reserva) {
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Aprobada")
        guardar()

    }

    fun rechazar(reserva: Reserva) {
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Rechazada")
        guardar()

    }

    fun agregarReserva(
        espacio: String,
        fecha: String,
        horario: String,
        motivo: String
    ) {
        val nuevoId = (reservas.maxOfOrNull { it.id } ?: 0) + 1
        reservas.add(Reserva(nuevoId, espacio, fecha, horario, motivo, "Pendiente"))
        guardar()

    }
}