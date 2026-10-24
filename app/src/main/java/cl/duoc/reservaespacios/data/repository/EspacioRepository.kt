package cl.duoc.reservaespacios.data.repository

import cl.duoc.reservaespacios.data.room.EspacioDao
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.espaciosDePrueba

class EspacioRepository(private val dao: EspacioDao){

    suspend fun cargarEspacios(): List<Espacio>{
        if(dao.obtenerTodos().isEmpty()) dao.insertarTodos(espaciosDePrueba)
        return dao.obtenerTodos()
    }

}