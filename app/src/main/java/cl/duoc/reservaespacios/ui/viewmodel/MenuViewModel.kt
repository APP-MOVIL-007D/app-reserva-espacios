package cl.duoc.reservaespacios.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuUiState(
    val nombre: String = "Nombre",
    val correo: String = "***@duoc.cl",
    val mostrarDialogoSalir: Boolean = false
)

sealed interface MenuEvento {
    data class Mensaje(val texto: String) : MenuEvento
    data object SesionCerrada : MenuEvento
}

class MenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val _eventos = Channel<MenuEvento>(Channel.BUFFERED)
    val eventos: Flow<MenuEvento> = _eventos.receiveAsFlow()


    fun pedirCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoSalir = true) }
    }

    fun cancelarCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoSalir = false) }
    }

    fun confirmarCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoSalir = false) }
        enviar(MenuEvento.SesionCerrada)
    }

    private fun enviar(evento: MenuEvento) {
        viewModelScope.launch { _eventos.send(evento) }
    }
}