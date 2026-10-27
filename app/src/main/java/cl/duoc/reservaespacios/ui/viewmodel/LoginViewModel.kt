package cl.duoc.reservaespacios.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.reservaespacios.data.repository.UsuarioRepository
import cl.duoc.reservaespacios.data.room.AppDatabase
import cl.duoc.reservaespacios.utils.validarCorreo
import cl.duoc.reservaespacios.utils.validarPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val errorCorreo: String? = null,
    val errorPassword: String? = null
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioRepository = UsuarioRepository(AppDatabase.getInstance(application).usuarioDao())

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoChange(valor: String) {
        _uiState.update { it.copy(correo = valor, errorCorreo = null) }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update { it.copy(password = valor, errorPassword = null) }
    }

    fun ingresar(onExito: (String) -> Unit) {
        val actual = _uiState.value
        val errorCorreo = validarCorreo(actual.correo)
        val errorPassword = validarPassword(actual.password)

        if (errorCorreo != null || errorPassword != null) {
            _uiState.update { it.copy(errorCorreo = errorCorreo, errorPassword = errorPassword) }
            return
        }

        val correoLimpio = actual.correo.trim().lowercase()

        viewModelScope.launch {
            val usuario = usuarioRepository.buscarPorCorreo(correoLimpio)
            when {
                usuario == null -> _uiState.update { it.copy(errorCorreo = "Usuario no registrado") }
                usuario.password != actual.password -> _uiState.update { it.copy(errorPassword = "Contraseña incorrecta") }
                else -> {
                    _uiState.value = LoginUiState()
                    onExito(correoLimpio)
                }
            }
        }
    }
}
