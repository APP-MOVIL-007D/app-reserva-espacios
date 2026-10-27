package cl.duoc.reservaespacios.utils

import java.util.Calendar

// "dd/MM/yyyy" (0 = hoy, 1 = Mañana, -2 = anteayer)
fun fechaDesdeHoy(dias: Int): String {
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DAY_OF_MONTH, dias)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)
    val mes = calendario.get(Calendar.MONTH) + 1 // ene empieza en 0
    val anio = calendario.get(Calendar.YEAR)
    return "%02d/%02d/%04d".format(dia, mes, anio)
}

// "26/10" -> 261026, "26/10/2026" -> 261026 (Compatibilidad 100% con ambas pantallas)
fun numeroFecha(fecha: String): Int {
    val partes = fecha.split("/")
    val dia = partes.getOrNull(0)?.toIntOrNull() ?: 0
    val mes = partes.getOrNull(1)?.toIntOrNull() ?: 0
    val anio = if (partes.size >= 3) {
        partes.getOrNull(2)?.toIntOrNull() ?: 0
    } else {
        Calendar.getInstance().get(Calendar.YEAR)
    }

    return (anio % 100) * 10000 + mes * 100 + dia
}

// "10:01 - 11:20" -> 1001
fun numeroHora(horario: String): Int =
    horario.take(5).replace(":", "").toIntOrNull() ?: 0

// "25/10/2026" -> "Hoy", "26/10/2026" -> "Mañana", otra fecha -> "27/10"
fun textoDia(fecha: String): String {
    val anioActual = Calendar.getInstance().get(Calendar.YEAR)
    val fechaCompleta = if (fecha.count { it == '/' } == 2) fecha else "$fecha/$anioActual"
    
    return when (fechaCompleta) {
        fechaDesdeHoy(0) -> "Hoy"
        fechaDesdeHoy(1) -> "Mañana"
        else -> if (fecha.count { it == '/' } == 2) fecha.substringBeforeLast("/") else fecha
    }
}

// Hora del celu
fun horaActual(): String {
    val calendario = Calendar.getInstance()
    return "%02d:%02d".format(calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE))
}

fun sumarMins(hora: String, minutos: Int): String {
    val total = hora.take(2).toInt() * 60 + hora.substring(3, 5).toInt() + minutos
    return "%02d:%02d".format(total / 60, total % 60)
}

// Modelos y utilidades de horario requeridas por el Calendario
data class BloqueHorario(val horaInicio: String, val horaFin: String)

val BLOQUES_HORARIOS_BASE = listOf(
    BloqueHorario("08:31", "09:50"),
    BloqueHorario("10:01", "11:20"),
    BloqueHorario("11:31", "12:50"),
    BloqueHorario("13:01", "14:20"),
    BloqueHorario("14:31", "15:50")
)

fun horaStringAMinutos(hora: String): Int {
    val partes = hora.trim().split(":")
    if (partes.size == 2) {
        return (partes[0].toIntOrNull() ?: 0) * 60 + (partes[1].toIntOrNull() ?: 0)
    }
    return 0
}
