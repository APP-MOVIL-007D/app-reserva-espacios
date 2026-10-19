package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

data class BloqueHorario(val horaInicio: String, val horaFin: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitarReservaScreen(
    navController: NavController,
    reservasViewModel: ReservasViewModel,
    espacioParam: String = "",
    fechaParam: String = "20/10",
    horaInicioParam: String = "08:31"
) {
    var espacio by remember { mutableStateOf(espacioParam) }
    var fecha by remember { mutableStateOf(fechaParam.replace("-", "/").ifEmpty { "20/10" }) }
    var horaInicio by remember { mutableStateOf(horaInicioParam.ifEmpty { "08:31" }) }
    var motivo by remember { mutableStateOf("") }

    val bloquesBase = listOf(
        BloqueHorario("08:31", "09:50"),
        BloqueHorario("10:01", "11:20"),
        BloqueHorario("11:31", "12:50"),
        BloqueHorario("13:01", "14:20"),
        BloqueHorario("14:31", "15:50")
    )

    val bloquesFinDisponibles = remember(horaInicio) {
        bloquesBase.filter { it.horaFin >= horaInicio }
    }

    var horaFinSeleccionada by remember(horaInicio) { 
        mutableStateOf(bloquesFinDisponibles.firstOrNull()?.horaFin ?: bloquesBase[0].horaFin) 
    }
    var expandedFin by remember { mutableStateOf(false) }

    val horarioFinal = "$horaInicio - $horaFinSeleccionada"

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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Solicitar Reserva",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        OutlinedTextField(
            value = espacio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Espacio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = horaInicio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hora de Inicio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Selector Hora de Fin (Filtrado para que sea mayor o igual a la hora de inicio)
        ExposedDropdownMenuBox(
            expanded = expandedFin,
            onExpandedChange = { expandedFin = !expandedFin },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = horaFinSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hora de Fin (Seleccionable)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFin) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedFin,
                onDismissRequest = { expandedFin = false }
            ) {
                bloquesFinDisponibles.forEach { bloque ->
                    DropdownMenuItem(
                        text = { Text(bloque.horaFin) },
                        onClick = {
                            horaFinSeleccionada = bloque.horaFin
                            expandedFin = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Horario final: $horarioFinal",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (motivo.isNotBlank()) {
                    reservasViewModel.agregarReserva(espacio, fecha, horarioFinal, motivo)
                    reservasViewModel.pestanaActual = "Calendario"
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar")
        }
    }
}
