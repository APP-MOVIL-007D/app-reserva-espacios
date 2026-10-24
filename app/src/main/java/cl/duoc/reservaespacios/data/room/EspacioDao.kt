package cl.duoc.reservaespacios.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.reservaespacios.model.Espacio

@Dao
interface EspacioDao{

    @Query("SELECT * FROM espacios ORDER BY nombre")
    suspend fun obtenerTodos(): List<Espacio>

    @Insert
    suspend fun insertar(espacio: Espacio)

    @Insert
    suspend fun insertarTodos(espacios: List<Espacio>)
}