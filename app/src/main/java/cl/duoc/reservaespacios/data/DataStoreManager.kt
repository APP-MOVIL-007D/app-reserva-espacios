package cl.duoc.reservaespacios.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context) {
    private  val gson = Gson()

    private  val RESERVA_KEY = stringPreferencesKey("reserva")

    suspend fun saveReserva(reserva: List<DatosSolicitarReserva>){
        val json = gson.toJson(reserva)
        context.dataStore.edit { prefs -> prefs[RESERVA_KEY] = json }
    }


}