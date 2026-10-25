package cl.duoc.reservaespacios.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Modelo de datos para representar un archivo subido
data class ArchivoCargado(
    val nombre: String,
    val peso: String,
    val fecha: String,
    val estado: String
)

class CargarHorarioViewModel : ViewModel() {

    var ultimaCarga by mutableStateOf<ArchivoCargado?>(
        ArchivoCargado(
            nombre = "horario_semestre.csv",
            peso = "3 MB",
            fecha = "10/10/2026",
            estado = "Listo"
        )
    )
        private set

    var mostrarDialogoConfirmacion by mutableStateOf(false)
        private set

    var uriArchivoPendiente by mutableStateOf<Uri?>(null)
        private set

    var nombreArchivoPendiente by mutableStateOf("")
        private set

    fun archivoSeleccionadoDesdeDispositivo(uri: Uri?, nombre: String) {
        if (uri != null) {
            uriArchivoPendiente = uri
            nombreArchivoPendiente = nombre
            mostrarDialogoConfirmacion = true
        }
    }

    fun cancelarCarga() {
        mostrarDialogoConfirmacion = false
        uriArchivoPendiente = null
        nombreArchivoPendiente = ""
    }

    fun confirmarCarga() {
        mostrarDialogoConfirmacion = false

        ultimaCarga = ArchivoCargado(
            nombre = nombreArchivoPendiente.ifEmpty { "archivo_nuevo.csv" },
            peso = "Calculando...",
            fecha = "Hoy",
            estado = "Procesando"
        )

        uriArchivoPendiente = null
        nombreArchivoPendiente = ""
    }
}