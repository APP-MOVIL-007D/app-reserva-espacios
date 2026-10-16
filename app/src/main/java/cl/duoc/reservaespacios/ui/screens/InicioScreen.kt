package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel
import java.util.Calendar

@Composable
fun InicioScreen(reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier){
    val proxima = reservasViewModel.reservas.firstOrNull {it.estado == "Aprobada" }
    val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    //localtime no esta en android 7
    val saludo = when(hora){
        in 5..11 -> "Buenos días"
        in 12..19 -> "Buenas tardes"
        else -> "Buenas noches"
    }

    val pendientes = reservasViewModel.reservas.count { it.estado == "Pendiente"}
    val aprobadas = reservasViewModel.reservas.count { it.estado == "Aprobada"}

    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text(saludo, style = MaterialTheme.typography.headlineMedium)
        Text("Tienes $pendientes y $aprobadas aprobadas", style = MaterialTheme.typography.bodyMedium)

        if(proxima != null){
            Spacer(modifier = Modifier.height(24.dp))
            Text("Tu próxima reserva", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth())  {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(proxima.espacio, style = MaterialTheme.typography.titleMedium)
                    Text("${proxima.fecha} · ${proxima.horario}")
                    Text(proxima.motivo, style = MaterialTheme.typography.bodyMedium)
                }
            }

        }else{
            Spacer(modifier = Modifier.height(24.dp))
            Text("No tienes reservas aprobadas", style = MaterialTheme.typography.bodyMedium)
        }

    }
}