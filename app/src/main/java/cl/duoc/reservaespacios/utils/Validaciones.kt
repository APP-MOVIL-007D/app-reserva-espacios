package cl.duoc.reservaespacios.utils
// 1. Constante global para que CampoPassword la pueda importar directamente
const val MIN_PASSWORD = 6

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

// 2. Funciones globales para que LoginScreen no falle si las llama directo
fun validarCorreo(correo: String): String? = Validaciones.validarCorreo(correo)
fun validarPassword(password: String): String? = Validaciones.validarPassword(password)

// 3. Objeto Validaciones para AdminNuevoEspacio
object Validaciones {

    fun validarNombreEspacio(nombre: String): String? {
        return if (nombre.isBlank()) "El nombre no puede estar vacío" else null
    }

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim().lowercase()
        if (limpio.isBlank()) return "Ingresa tu correo"
        if (!EMAIL_REGEX.matches(limpio)) return "Ingresa un correo válido"
        if (limpio.endsWith("@duocuc.cl")) return "Esta app es solo para docentes y administrativos"
        if (!limpio.endsWith("@duoc.cl") && !limpio.endsWith("@profesor.duoc.cl")) {
            return "Usa tu correo institucional Duoc UC"
        }
        return null
    }

    fun validarPassword(password: String): String? {
        if (password.isBlank()) return "Ingresa tu contraseña"
        if (password.length < MIN_PASSWORD) return "Debe tener al menos $MIN_PASSWORD caracteres"
        return null
    }
}