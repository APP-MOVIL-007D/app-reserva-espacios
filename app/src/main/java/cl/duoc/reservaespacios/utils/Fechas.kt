package cl.duoc.reservaespacios.utils

import java.util.Calendar

//falta año acá

// "dd/MM" (0=hoy, 1 = Mañana, -2 = anteayer)
fun fechaDesdeHoy(dias: Int): String {
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DAY_OF_MONTH, dias)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)
    val mes = calendario.get(Calendar.MONTH) + 1 //ene empieza en 0
    return "%02d/%02d".format(dia, mes)
}

//"26/10" -> 1026
fun numeroFecha(fecha: String): Int {
    val dia = fecha.substringBefore("/").toIntOrNull() ?: 0
    val mes = fecha.substringAfter("/").toIntOrNull() ?: 0
    //
    return mes * 100 + dia
}

// "10:01 - 11:20" -> 1001
fun numeroHora(horario: String): Int =
    horario.take(5).replace(":", "").toIntOrNull() ?: 0

// "25/10" -> Sería "Hoy", "26/10" -> Mañana, otra fecha quedaría igual
fun textoDia(fecha: String): String = when (fecha) {
    fechaDesdeHoy(0) -> "Hoy"
    fechaDesdeHoy(1) -> "Mañana"
    else -> fecha
}

//hora del celu

fun horaActual(): String {
    val calendario = Calendar.getInstance()
    return "%02d:%02d".format(calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE))
}

fun sumarMins(hora: String, minutos: Int): String{
    val total = hora.take(2).toInt() * 60 + hora.substring(3,5).toInt() + minutos
    return "%02d:%02d".format(total/60, total % 60)
}