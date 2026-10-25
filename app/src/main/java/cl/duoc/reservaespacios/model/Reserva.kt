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

val reservasDePrueba = listOf(
    Reserva(1, "Laboratorio LC4", fechaDesdeHoy(1), "10:01 - 11:20", "Desarrollo de Aplicaciones Móviles", "Aprobada",
        correoUsuario = "nan.bernal@profesor.duoc.cl"),
    Reserva(2, "Laboratorio LC5", "16/10", "11:01 - 12:20", "Fundamentos de Programación", "Aprobada"),
    Reserva(3, "Estudio Sonido 1", "17/10", "14:01 - 15:20", "Grabación de podcast", "Aprobada"),
    Reserva(4, "Sala S302", "20/10", "08:01 - 09:20", "Reunión de coordinación", "Pendiente"),
    Reserva(5, "Set TV", "21/10", "12:31 - 13:50", "Práctica de cámara", "Pendiente"),
    Reserva(6, "Anfiteatro", "23/10", "15:31 - 16:50", "Charla de titulación", "Pendiente"),
    Reserva(7, "Sala S604", "14/10", "09:31 - 10:50", "Ayudantía de matemáticas", "Rechazada"),
    Reserva(8, "Salas de Actuación", "18/10", "11:01 - 12:20", "Ensayo de teatro", "Rechazada"),
    Reserva(9, "Laboratorio de Diseño", "15/10", "14:01 - 15:20", "Revisión de portafolio", "Cancelada"),
    Reserva(10, "Sala S54", "22/10", "08:01 - 09:20", "La Sociedad del Futuro", "Cancelada")
)
