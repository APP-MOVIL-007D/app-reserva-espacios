package cl.duoc.reservaespacios.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.duoc.reservaespacios.model.Reserva
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()

    private val RESERVA_KEY = stringPreferencesKey("reserva")

    suspend fun guardarReservas(reservas: List<Reserva>) {
        val json = gson.toJson(reservas)
        context.dataStore.edit { prefs -> prefs[RESERVA_KEY] = json}
    }

    suspend fun cargarReservas(): List<Reserva> ? {
        val json = context.dataStore.data.first()[RESERVA_KEY] ?: return null
        val tipo = object : TypeToken<List<Reserva>>() {}.type
        return gson.fromJson(json, tipo)
    }



}