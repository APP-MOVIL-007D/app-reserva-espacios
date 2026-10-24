package cl.duoc.reservaespacios.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MenuViewModel2 : ViewModel() {

    // Variables que controlan si se ven los cuadros de diálogo o no
    var mostrarToastCerrarSesion by mutableStateOf(false)
        private set

    var mostrarToastFoto by mutableStateOf(false)
        private set

    // Funciones para Cerrar Sesión
    fun pedirCerrarSesion() {
        mostrarToastCerrarSesion = true
    }

    fun cancelarCerrarSesion() {
        mostrarToastCerrarSesion = false
    }

    fun confirmarCerrarSesion(onNavegarLogin: () -> Unit) {
        mostrarToastCerrarSesion = false
        onNavegarLogin()
    }

    // Funciones para la Foto de Perfil
    fun pedirCambiarFoto() {
        mostrarToastFoto = true
    }

    fun cancelarCambiarFoto() {
        mostrarToastFoto = false
    }

    fun procesarSeleccionCamara(onAbrirCamara: () -> Unit) {
        mostrarToastFoto = false
        onAbrirCamara()
    }

    fun procesarSeleccionGaleria(onAbrirGaleria: () -> Unit) {
        mostrarToastFoto = false
        onAbrirGaleria()
    }

    // Foto de perfil
    var fotoBitmap by mutableStateOf<Bitmap?>(null)
        private set

    // Función para guardar la foto una vez tomada/seleccionada
    fun actualizarFoto(bitmap: Bitmap?) {
        fotoBitmap = bitmap
    }

}