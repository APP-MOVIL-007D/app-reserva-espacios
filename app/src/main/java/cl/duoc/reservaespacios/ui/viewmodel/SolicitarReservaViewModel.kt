package cl.duoc.reservaespacios.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.reservaespacios.utils.BLOQUES_HORARIOS_BASE
import cl.duoc.reservaespacios.utils.BloqueHorario

class SolicitarReservaViewModel : ViewModel() {

    // Estados del formulario
    var espacio by mutableStateOf("")
        private set
    var fecha by mutableStateOf("")
        private set
    var horaInicio by mutableStateOf("")
        private set
    var horaFinSeleccionada by mutableStateOf("")
        private set
    var motivo by mutableStateOf("")
        private set

    // Control de Dropdown
    var dropdownFinAbierto by mutableStateOf(false)
        private set

    // Estado de carga inicial
    var inicializado by mutableStateOf(false)
        private set

    //
    var errorMotivo by mutableStateOf<String?>(null)
        private set

    fun cargarDatosIniciales(espacioParam: String, fechaParam: String, horaInicioParam: String) {
        if (!inicializado) {
            espacio = espacioParam
            fecha = fechaParam.replace("-", "/").ifEmpty { "20/10" }
            horaInicio = horaInicioParam.ifEmpty { "08:31" }

            // Asigna el primer bloque de fin disponible como valor por defecto
            horaFinSeleccionada = obtenerBloquesFinDisponibles().firstOrNull()?.horaFin ?: BLOQUES_HORARIOS_BASE.first().horaFin

            inicializado = true
        }
    }

    // Retorna los bloques de fin que sean mayores o iguales a la hora de inicio seleccionada usando BLOQUES_HORARIOS_BASE
    fun obtenerBloquesFinDisponibles(): List<BloqueHorario> {
        return BLOQUES_HORARIOS_BASE.filter { it.horaFin >= horaInicio }
    }

    // Funciones para interactuar con la interfaz
    fun cambiarMotivo(nuevoMotivo: String) {
        motivo = nuevoMotivo
        errorMotivo = null
    }

    fun mensajeErrorMotivo(mensaje: String) {
        errorMotivo = mensaje
    }

    fun alternarDropdownFin(abierto: Boolean) {
        dropdownFinAbierto = abierto
    }

    fun seleccionarHoraFin(nuevaHoraFin: String) {
        horaFinSeleccionada = nuevaHoraFin
        dropdownFinAbierto = false
    }

    // Retorna el string con el formato "08:31 - 09:50" para guardar en la base de datos
    fun obtenerHorarioFinal(): String {
        return "$horaInicio - $horaFinSeleccionada"
    }

    // Valida que el formulario esté listo para enviarse
    fun formularioEsValido(): Boolean {
        return motivo.isNotBlank()
    }
}