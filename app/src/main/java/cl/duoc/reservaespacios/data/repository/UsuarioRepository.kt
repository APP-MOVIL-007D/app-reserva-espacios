package cl.duoc.reservaespacios.data.repository

import cl.duoc.reservaespacios.data.room.UsuarioDao
import cl.duoc.reservaespacios.model.Usuario
import cl.duoc.reservaespacios.model.usuariosDePrueba

class UsuarioRepository(private val dao: UsuarioDao){
    suspend fun cargarUsuarios(): List<Usuario>{
        if (dao.obtenerTodos().isEmpty()) dao.insertarTodos(usuariosDePrueba)
        return dao.obtenerTodos()
    }
}