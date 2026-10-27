package cl.duoc.reservaespacios.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duoc.reservaespacios.model.Usuario

@Dao
interface UsuarioDao{

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos() : List<Usuario>

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Insert
    suspend fun insertarTodos(usuarios: List<Usuario>)

    @Update
    suspend fun actualizar(usuario: Usuario)

}
