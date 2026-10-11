package cl.duoc.reservaespacios.utils

//función para validar el correo (Simple)
fun validarCorreo(correo: String): String? {
    val limpio = correo.trim().lowercase()

    if (limpio.isBlank()) return "Ingresa tu correo"
    if (!limpio.endsWith("@duocuc.cl") && !limpio.endsWith("@profesor.duocuc.cl")) {
        return "Usa tu correo institucional Duoc Uc"
    }
    return null
}

fun validarPassword(password: String): String? {

    if (password.isBlank()) return "Ingresa tu contraseña"
    if (password.length < 6) return "Debe tener al menos 6 caracteres"

    return null
}