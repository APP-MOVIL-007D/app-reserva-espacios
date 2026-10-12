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
        Reserva(1,"Sala S41", "20/10", "10:01 - 11:20", "Clase de proyecto", "Aprobada"),
        Reserva(2,"Sala Estudio Sonido 1", "21/12", "09:01 - 12:20", "Clase de Sonido", "Rechazada"),
        Reserva(3,"Sala LC13", "09/09", "08:01 - 09:20", "Clase de algo", "Pendiente")
    )
