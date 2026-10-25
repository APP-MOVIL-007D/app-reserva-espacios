package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.reservaespacios.ui.viewmodel.SolicitarReservaViewModel
import cl.duoc.reservaespacios.ui.viewmodel.BloqueHorario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitarReservaScreen(
    viewModel: SolicitarReservaViewModel = viewModel (),
    modifier: Modifier = Modifier,
    // Parametros de navegacion iniciales
    espacioParam: String = "",
    fechaParam: String = "",
    horaInicioParam: String = "",
    // Lambda
    onVolver: () -> Unit,
    onGuardarReserva: (espacio: String, fecha: String, horarioCompleto: String, motivo: String) -> Unit
) {

    // Inicializamos el ViewModel con los parámetros que recibe la pantalla cuando se abre
    LaunchedEffect (Unit) {
        viewModel.cargarDatosIniciales(espacioParam, fechaParam, horaInicioParam)
    }

    // Estructura del SolicitarReserva
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton (onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Solicitar Reserva",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        // Nombre del Espacio (Solo Lectura)
        OutlinedTextField(
            value = viewModel.espacio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Espacio") },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(12.dp))

        // Fecha (Solo Lectura)
        OutlinedTextField(
            value = viewModel.fecha,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha") },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(12.dp))

        // Hora de Inicio (Solo Lectura)
        OutlinedTextField(
            value = viewModel.horaInicio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hora de Inicio") },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(12.dp))

        // Hora de Fin
        SelectorHoraFin(
            horaFinSeleccionada = viewModel.horaFinSeleccionada,
            dropdownAbierto = viewModel.dropdownFinAbierto,
            bloquesDisponibles = viewModel.obtenerBloquesFinDisponibles(),
            onAlternarDropdown = { viewModel.alternarDropdownFin(it) },
            onSeleccionarHora = { viewModel.seleccionarHoraFin(it) }
        )

        Spacer(modifier = modifier.height(12.dp))

        // horario completo
        Text(
            text = "Horario final: ${viewModel.obtenerHorarioFinal()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = modifier.height(12.dp))

        // Motivo
        OutlinedTextField(
            value = viewModel.motivo,
            onValueChange = { nuevoMotivo -> viewModel.cambiarMotivo(nuevoMotivo) },
            label = { Text("Motivo") },
            modifier = modifier.fillMaxWidth()
        )

        Spacer(modifier = modifier.height(24.dp))

        // Botón Reservar
        Button (
            onClick = {
                if (viewModel.formularioEsValido()) {
                    onGuardarReserva(
                        viewModel.espacio,
                        viewModel.fecha,
                        viewModel.obtenerHorarioFinal(),
                        viewModel.motivo
                    )
                }
            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Reservar")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHoraFin(
    modifier: Modifier = Modifier,
    horaFinSeleccionada: String,
    dropdownAbierto: Boolean,
    bloquesDisponibles: List<BloqueHorario>,
    onAlternarDropdown: (Boolean) -> Unit,
    onSeleccionarHora: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = dropdownAbierto,
        onExpandedChange = { onAlternarDropdown(it) },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = horaFinSeleccionada,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hora de Fin (Seleccionable)") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownAbierto)
            },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = dropdownAbierto,
            onDismissRequest = { onAlternarDropdown(false) }
        ) {
            bloquesDisponibles.forEach { bloque ->
                DropdownMenuItem(
                    text = { Text(bloque.horaFin) },
                    onClick = { onSeleccionarHora(bloque.horaFin) }
                )
            }
        }
    }
}
