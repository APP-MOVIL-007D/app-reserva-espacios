package cl.duoc.reservaespacios.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.reservasDePrueba

class ReservasViewModel : ViewModel() {
    val reservas = mutableStateListOf<Reserva>()

    init{
        reservas.addAll(reservasDePrueba)
    }

    fun cancelar(reserva: Reserva){
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Cancelada")
    }
    fun aprobar(reserva: Reserva){
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Aprobada")
    }

    fun rechazar(reserva: Reserva){
        val index = reservas.indexOfFirst { it.id == reserva.id }
        reservas[index] = reserva.copy(estado = "Rechazada")
    }
}