package cl.duoc.reservaespacios.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.Usuario

//si llego a cambiar una columna, lo ideal sería version++
@Database(entities = [Reserva::class, Espacio::class, Usuario::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun reservaDao(): ReservaDao
    abstract fun espacioDao(): EspacioDao
    abstract fun usuarioDao(): UsuarioDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reservas_db"
                ).fallbackToDestructiveMigration(true).build()
                    .also { INSTANCE = it }
            }
        }

    }

}