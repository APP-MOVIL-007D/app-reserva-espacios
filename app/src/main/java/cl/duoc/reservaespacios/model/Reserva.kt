package cl.duoc.reservaespacios.model

data class Reserva(
    val id: Int,
    val espacio: String,
    val fecha: String,
    val horario: String,
    val motivo: String,
    val estado: String


)

    val reservasDePrueba = listOf(
        Reserva(1,"Sala 604", "20/10", "10:01 - 11:20", "Clase de proyecto", "Aprobada"),
        Reserva(2,"Sala 134", "21/12", "09:01 - 12:20", "Clase de programación", "Rechazada"),
        Reserva(3,"Sala LC13", "09/09", "08:01 - 15:20", "Clase de algo", "Pendiente")
    )
