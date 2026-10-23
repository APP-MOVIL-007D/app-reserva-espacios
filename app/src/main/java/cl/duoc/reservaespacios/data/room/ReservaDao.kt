package cl.duoc.reservaespacios.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duoc.reservaespacios.model.Reserva

@Dao
interface ReservaDao {

    @Query("SELECT * FROM reservas ORDER BY id")
    suspend fun obtenerTodas() : List<Reserva>


    @Insert
    suspend fun insertar(reserva: Reserva)

    @Insert
    suspend fun insertarTodas(reservas : List<Reserva>)


    @Update
    suspend fun actualizar(reserva: Reserva)


}