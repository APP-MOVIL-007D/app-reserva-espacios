package cl.duoc.reservaespacios.utils

//una o más letras, números o simbolos
private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

const val MIN_PASSWORD = 6

//función para validar el correo (Simple)
fun validarCorreo(correo: String): String? {
    val limpio = correo.trim().lowercase()

    if (limpio.isBlank()) return "Ingresa tu correo"
    if(!EMAIL_REGEX.matches(limpio)) return "Ingresa un correo válido"
    if(limpio.endsWith("@duocuc.cl")) return "Esta app es solo para docentes y administrativos"
    if (!limpio.endsWith("@duoc.cl") && !limpio.endsWith("@profesor.duoc.cl")) {
        return "Usa tu correo institucional Duoc Uc"
    }
    return null
}

fun validarPassword(password: String): String? {

    if (password.isBlank()) return "Ingresa tu contraseña"
    if (password.length < MIN_PASSWORD) return "Debe tener al menos $MIN_PASSWORD caracteres"

    return null
}