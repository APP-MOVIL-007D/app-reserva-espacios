package cl.duoc.reservaespacios.utils

import java.util.Calendar

//falta año acá

// "dd/MM" (0=hoy, 1 = Mañana, -2 = anteayer)
fun fechaDesdeHoy(dias: Int) : String{
    val calendario = Calendar.getInstance()
    calendario.add(Calendar.DAY_OF_MONTH, dias)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)
    val mes = calendario.get(Calendar.MONTH) + 1 //ene empieza en 0
    return "%02d/%02d".format(dia, mes)
}

//"26/10" -> 1026
fun numeroFecha(fecha: String) : Int{
    val dia = fecha.substringBefore("/").toIntOrNull() ?:0
    val mes = fecha.substringAfter("/").toIntOrNull() ?:0
    //
    return mes * 100 + dia
}

// "10:01 - 11:20" -> 1001
fun numeroHora(horario: String) : Int =
    horario.take(5).replace(":", "").toIntOrNull() ?: 0

