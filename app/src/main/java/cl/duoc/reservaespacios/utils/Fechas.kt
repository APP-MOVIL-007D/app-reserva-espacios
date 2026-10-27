package cl.duoc.reservaespacios.utils

import java.util.Calendar

//falta año acá

// "dd/MM" o "dd/MM/yyyy" (0=hoy, 1 = Mañana, -2 = anteayer)
fun fechaDesdeHoy(dias: Int) : String{
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DAY_OF_MONTH, dias)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)
    val mes = calendario.get(Calendar.MONTH) + 1 //ene empieza en 0
    val anio = calendario.get(Calendar.YEAR)
    return "%02d/%02d/%04d".format(dia, mes, anio)
}

//"26/10" -> 1026, "26/10/2024" -> 20241026
fun numeroFecha(fecha: String) : Int{
    val partes = fecha.split("/")
    val dia = partes.getOrNull(0)?.toIntOrNull() ?: 0
    val mes = partes.getOrNull(1)?.toIntOrNull() ?: 0
    
    return if (partes.size >= 3) {
        val anio = partes.getOrNull(2)?.toIntOrNull() ?: 0
        (anio % 100) * 10000 + mes * 100 + dia // Ej: 2024 -> 241026
    } else {
        mes * 100 + dia
    }
}

// "10:01 - 11:20" -> 1001
fun numeroHora(horario: String) : Int =
    horario.take(5).replace(":", "").toIntOrNull() ?: 0

// "25/10/2024" -> Sería "Hoy"
fun textoDia(fecha: String) : String = when(fecha) {
    fechaDesdeHoy(0) -> "Hoy"
    fechaDesdeHoy(1) -> "Mañana"
    else -> fecha.substringBeforeLast("/") // Devuelve solo "dd/MM" visualmente si es otra fecha
}

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