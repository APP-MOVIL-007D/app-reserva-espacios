package cl.duoc.reservaespacios.data.repository

import android.graphics.Bitmap
import cl.duoc.reservaespacios.data.room.EspacioDao
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.espaciosDePrueba
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class EspacioRepository(private val dao: EspacioDao, private val carpetaFotos: File){

    suspend fun cargarEspacios(): List<Espacio>{
        if(dao.obtenerTodos().isEmpty()) dao.insertarTodos(espaciosDePrueba)
        return dao.obtenerTodos()
    }

    suspend fun agregar(espacio: Espacio){
        dao.insertar(espacio)
    }

    suspend fun guardarFoto(nombreEspacio: String, foto: Bitmap): String = withContext(Dispatchers.IO) {
        val archivo = File(carpetaFotos, "espacio_${nombreEspacio.lowercase().replace(" ", "_")}.jpg")
        archivo.outputStream().use { salida ->
            foto.compress(Bitmap.CompressFormat.JPEG, 85, salida)
        }
        archivo.absolutePath
    }

}
