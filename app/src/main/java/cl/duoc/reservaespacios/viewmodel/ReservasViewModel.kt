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
}