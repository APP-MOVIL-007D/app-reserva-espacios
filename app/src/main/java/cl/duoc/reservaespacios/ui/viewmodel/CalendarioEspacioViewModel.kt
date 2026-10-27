package cl.duoc.reservaespacios.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.utils.BloqueHorario
import cl.duoc.reservaespacios.utils.fechaDesdeHoy
import cl.duoc.reservaespacios.utils.horaStringAMinutos
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class DiaSemana(val nombre: String, val numero: Int, val fechaCompleta: String, val esPasado: Boolean = false)

class CalendarioEspacioViewModel : ViewModel() {

    var fechaSeleccionada by mutableStateOf(obtenerFechaActual())
        private set

    var mesActualOffset by mutableIntStateOf(0)
        private set

    fun cambiarFechaSeleccionada(nuevaFecha: String) {
        fechaSeleccionada = nuevaFecha
    }

    fun avanzarMes() {
        if (mesActualOffset < 12) mesActualOffset++
    }

    fun retrocederMes() {
        if (mesActualOffset > 0) mesActualOffset--
    }

    fun seleccionarFechaDesdePicker(utcMillis: Long) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = utcMillis
        }
        val dia = cal.get(Calendar.DAY_OF_MONTH)
        val mes = cal.get(Calendar.MONTH) + 1
        val anio = cal.get(Calendar.YEAR)

        val selectedDateStr = "%02d/%02d/%04d".format(dia, mes, anio)
        fechaSeleccionada = selectedDateStr

        val today = Calendar.getInstance()
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth = today.get(Calendar.MONTH)

        val targetMonth = mes - 1
        val offset = (anio - todayYear) * 12 + (targetMonth - todayMonth)

        if (offset in 0..12) {
            mesActualOffset = offset
        }
    }

    private fun obtenerFechaActual(): String {
        val today = Calendar.getInstance()
        val todayDiaStr = today.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        val todayMesStr = (today.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val todayAnioStr = today.get(Calendar.YEAR).toString()
        return "$todayDiaStr/$todayMesStr/$todayAnioStr"
    }

    fun obtenerTextoMesAnio(): String {
        val calendario = Calendar.getInstance().apply {
            add(Calendar.MONTH, mesActualOffset)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val locCl = Locale.forLanguageTag("es-CL")
        val mesFormat = SimpleDateFormat("MMMM yyyy", locCl)
        return mesFormat.format(calendario.time).replaceFirstChar { it.uppercase() }
    }

    fun generarDiasDelMes(): List<DiaSemana> {
        val calendario = Calendar.getInstance().apply {
            add(Calendar.MONTH, mesActualOffset)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val diasDelMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)
        val anioActual = calendario.get(Calendar.YEAR)
        val mesActual = calendario.get(Calendar.MONTH) + 1
        val locCl = Locale.forLanguageTag("es-CL")

        val diaFormat = SimpleDateFormat("EEE", locCl)
        val calToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return (1..diasDelMes).map { dia ->
            val calDia = Calendar.getInstance().apply {
                set(Calendar.YEAR, anioActual)
                set(Calendar.MONTH, mesActual - 1)
                set(Calendar.DAY_OF_MONTH, dia)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val esPasado = calDia.timeInMillis < calToday.timeInMillis
            val nombre = diaFormat.format(calDia.time).replaceFirstChar { it.uppercase() }.take(3).replace(".", "")
            val diaStr = dia.toString().padStart(2, '0')
            val mesStr = mesActual.toString().padStart(2, '0')
            val anioStr = anioActual.toString()

            DiaSemana(nombre, dia, "$diaStr/$mesStr/$anioStr", esPasado)
        }
    }

    private fun fechasCoinciden(f1: String, f2: String): Boolean {
        if (f1 == f2) return true
        val f1Limpia = if (f1.count { it == '/' } == 2) f1.substringBeforeLast("/") else f1
        val f2Limpia = if (f2.count { it == '/' } == 2) f2.substringBeforeLast("/") else f2
        return f1Limpia == f2Limpia
    }

    fun obtenerEstadoBloque(bloque: BloqueHorario, espacioNombre: String, reservas: List<Reserva>): String {
        val bloqueInicioMin = horaStringAMinutos(bloque.horaInicio)
        val bloqueFinMin = horaStringAMinutos(bloque.horaFin)

        // 1. Comprobar si existe una reserva registrada (Aprobada o Pendiente)
        val reservaEncontrada = reservas.find { r ->
            if (r.espacio == espacioNombre && fechasCoinciden(r.fecha, fechaSeleccionada) && (r.estado == "Pendiente" || r.estado == "Aprobada")) {
                val partes = r.horario.split("-").map { it.trim() }
                if (partes.size == 2) {
                    val resInicioMin = horaStringAMinutos(partes[0])
                    val resFinMin = horaStringAMinutos(partes[1])
                    resInicioMin < bloqueFinMin && resFinMin > bloqueInicioMin
                } else {
                    r.horario == "${bloque.horaInicio} - ${bloque.horaFin}"
                }
            } else {
                false
            }
        }

        if (reservaEncontrada != null) {
            return reservaEncontrada.estado
        }

        // 2. Si el bloque está libre, comprobar si es el día de hoy y la hora del bloque ya pasó
        val todayStr = fechaDesdeHoy(0)
        if (fechasCoinciden(fechaSeleccionada, todayStr)) {
            val now = Calendar.getInstance()
            val nowMin = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
            if (bloqueInicioMin <= nowMin) {
                return "Pasado"
            }
        }

        return "Disponible"
    }
}
