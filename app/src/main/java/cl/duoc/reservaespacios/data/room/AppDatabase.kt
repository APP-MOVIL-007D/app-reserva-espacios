package cl.duoc.reservaespacios.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.Reserva

//si llego a cambiar una columna, lo ideal sería version++
@Database(entities = [Reserva::class, Espacio::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun reservaDao(): ReservaDao
    abstract fun espacioDao(): EspacioDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reservas_db"
                ).build().also { INSTANCE = it }
            }
        }

    }

}