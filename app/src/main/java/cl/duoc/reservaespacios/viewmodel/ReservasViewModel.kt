package cl.duoc.reservaespacios.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.reservaespacios.data.DataStoreManager
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.reservasDePrueba
import kotlinx.coroutines.launch

class ReservasViewModel(application: Application): AndroidViewModel(application) {
    val reservas = mutableStateListOf<Reserva>()
    private val dataStore = DataStoreManager(application)

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