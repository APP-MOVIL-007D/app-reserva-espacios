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
    if(!password.any { it.isDigit()}) return "Debe tener al menos un número"

    return null
}

fun validarNombreEspacio(nombre: String, yaExiste: Boolean): String? {
    if (nombre.isBlank()) return "El nombre es obligatorio"
    if (yaExiste) return "Ya existe un espacio con ese nombre"
    return null
}

fun validarCapacidad(capacidad: String): String? {
    if (capacidad.isBlank()) return "Ingresa la capacidad"
    val numero = capacidad.toIntOrNull() ?: return "Solo números"
    if (numero < 1) return "Tiene que ser al menos 1 persona"
    if (numero > 500) return "Máximo 500 personas"
    return null
}

fun validarUbicacion(ubicacion: String): String? {
    if (ubicacion.isBlank()) return "Ingresa dónde está el espacio"
    return null
}