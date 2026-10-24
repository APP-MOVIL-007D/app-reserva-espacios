package cl.duoc.reservaespacios.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.reservaespacios.model.Usuario

@Dao
interface UsuarioDao{

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos() : List<Usuario>

    @Insert
    suspend fun insertarTodos(usuarios: List<Usuario>)

}