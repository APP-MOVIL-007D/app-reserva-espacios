package cl.duoc.reservaespacios.data

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class DatosSolicitarReserva(
    val fecha: String,
    val bloqueHorario: String,
    val descripcion: String,
    val capacidad: String
)

class AppState(private val dataStoreManager: DataStoreManager) {
    val reservas = mutableStateListOf<DatosSolicitarReserva>()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun agregarReserva(fecha: String, bloqueHorario: String, descripcion: String, capacidad: String) {
        val nuevaReserva = DatosSolicitarReserva(fecha, bloqueHorario, descripcion, capacidad)
        reservas.add(nuevaReserva)
        scope.launch {

            dataStoreManager.saveReserva(reservas.toList())
        }
    }

}