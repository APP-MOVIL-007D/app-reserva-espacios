package cl.duoc.reservaespacios.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.duoc.reservaespacios.utils.fechaDesdeHoy

@Entity(tableName = "reservas")
data class Reserva(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val espacio: String,
    val fecha: String,
    val horario: String,
    val motivo: String,
    val estado: String,
    val correoUsuario: String = "",
    val horaIngreso : String = ""
)

private const val NANCY = "nan.bernal@profesor.duoc.cl"
private const val JAVIER = "jav.pena@profesor.duoc.cl"
private const val LEONARDO = "leo.hernandez@profesor.duoc.cl"
private const val NATALIA = "nat.herrera@profesor.duoc.cl"
private const val VERONICA = "ver.lobos@profesor.duoc.cl"
private const val MARIA_JESUS = "mar.alvarez@profesor.duoc.cl"
private const val ALEJANDRA = "agajardo@duoc.cl"

val reservasDePrueba = listOf(
    Reserva(1, "Laboratorio LC4", fechaDesdeHoy(0), "14:31 - 15:50", "Recuperación de clase DSY1105", "Aprobada", NANCY),
    Reserva(2, "Laboratorio LC5", fechaDesdeHoy(3), "10:01 - 11:20", "Ayudantía de Aplicaciones Móviles", "Pendiente", NANCY),
    Reserva(3, "Laboratorio LC3", fechaDesdeHoy(2), "08:31 - 09:50", "Prueba atrasada de Estadística", "Pendiente", NATALIA),
    Reserva(4, "Laboratorio LC5", fechaDesdeHoy(1), "13:01 - 14:20", "Revisión de proyecto Fullstack", "Aprobada", JAVIER),
    Reserva(5, "Sala S81", fechaDesdeHoy(4), "14:31 - 15:50", "Ayudantía de inglés", "Pendiente", VERONICA),
    Reserva(6, "Sala S81", fechaDesdeHoy(4), "14:31 - 15:50", "Taller de conversación", "Pendiente", MARIA_JESUS),
    Reserva(7, "Laboratorio LC5", fechaDesdeHoy(5), "08:31 - 09:50", "Reforzamiento de Base de Datos", "Aprobada", LEONARDO),
    Reserva(8, "Anfiteatro", fechaDesdeHoy(8), "11:31 - 12:50", "Charla de titulación", "Pendiente", ALEJANDRA),
    Reserva(9, "Laboratorio LC4", fechaDesdeHoy(-3), "08:31 - 09:50", "Recuperación de clase DSY1105", "Cancelada", NANCY),
    Reserva(10, "Sala S608", fechaDesdeHoy(-5), "11:31 - 12:50", "Ensayo de presentaciones", "Rechazada", NANCY),
    Reserva(11, "Laboratorio LC4", fechaDesdeHoy(7), "10:01 - 11:20", "Clase de reforzamiento DSY1105", "Aprobada", NANCY)
)
