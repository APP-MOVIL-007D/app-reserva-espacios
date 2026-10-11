package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.model.reservasDePrueba

@Composable
fun MisReservasScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis reservas", style = MaterialTheme.typography.headlineMedium)


        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(reservasDePrueba) { reserva ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(reserva.estado, style = MaterialTheme.typography.labelMedium)
                        Text(reserva.espacio, style = MaterialTheme.typography.titleMedium)
                        Text("${reserva.fecha} · ${reserva.horario}")
                        Text(reserva.motivo, style = MaterialTheme.typography.bodyMedium)

                    }
                }
            }
        }
    }


}