package cl.duoc.reservaespacios.data.repository

import android.content.Context
import androidx.core.content.edit

class SesionRepository (context: Context){
    private val prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun obtenerCorreo(): String = prefs.getString("correo", "") ?: ""

    fun guardarCorreo(correo : String) {
        prefs.edit { putString("correo", correo)}
    }

    fun cerrar() {
        prefs.edit { remove("correo")}
    }

    fun obtenerModoOscuro(): Boolean = prefs.getBoolean("modo_oscuro", false)

    fun guardarModoOscuro(esOscuro: Boolean) {
        prefs.edit { putBoolean("modo_oscuro", esOscuro) }
    }
}
