package cl.duoc.reservaespacios.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel

@Composable
fun SolicitarReservaScreen(
    navController: NavController,
    reservasViewModel: ReservasViewModel,
    espacioParam: String = ""
) {

    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var espacio by remember { mutableStateOf(espacioParam) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

            IconButton(
                onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Solicitar Reserva",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        OutlinedTextField(
            value = espacio,
            onValueChange = { espacio = it },
            label = { Text("Espacio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Bloque Horario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth()
        )

        //Boton para reservar
        Button(
            onClick = {
                reservasViewModel.agregarReserva(espacio, fecha, horario, motivo)
                reservasViewModel.pestanaActual = "Calendario"
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar")
        }
    }
}

