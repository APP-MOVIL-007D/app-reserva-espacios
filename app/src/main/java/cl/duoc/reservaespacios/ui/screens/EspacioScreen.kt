package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.reservaespacios.ui.theme.ReservaEspaciosTheme
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel

data class Sala(val nombre: String, val capacidad: Int)

@Composable
fun EspacioScreen(navController: NavController, reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier) {
    EspacioScreenContent(
        reservas = reservasViewModel.reservas,
        onNavigateToSolicitarReserva = { espacioNombre ->
            navController.navigate("calendarioEspacio/$espacioNombre")
        },
        modifier = modifier
    )
}

@Composable
fun EspacioScreenContent(
    reservas: List<Reserva>,
    onNavigateToSolicitarReserva: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var buscarEspacio by remember { mutableStateOf("") }


    val espaciosBase = listOf(
        Sala("Sala 01", 30),
        Sala("Sala 02", 25)
    )

    val espaciosDesdeDB = reservas
        .map { it.espacio }
        .distinct()
        .filter { nombre -> espaciosBase.none { it.nombre == nombre } }
        .map { Sala(it, 30) }

    val listaEspacios = espaciosBase + espaciosDesdeDB


    val espaciosFiltrados = listaEspacios.filter {
        it.nombre.contains(buscarEspacio, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    )
    {

        Text(
            text = "Espacios",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscarEspacio,
            onValueChange = { buscarEspacio = it },
            label = { Text("Buscar espacio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Espacios encontrados: ${espaciosFiltrados.size}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(espaciosFiltrados) { espacio ->

                val reservaActiva = reservas.find {
                    it.espacio == espacio.nombre && (it.estado == "Aprobada" || it.estado == "Pendiente")
                }
                val estaOcupado = reservaActiva != null

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = espacio.nombre,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Capacidad de personas: ${espacio.capacidad}",
                            style = MaterialTheme.typography.bodySmall,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onNavigateToSolicitarReserva(espacio.nombre) },
                            enabled = !estaOcupado,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(if (estaOcupado) "El espacio ya está en uso" else "Solicitar espacio") }
                    }
                }
            }
        }
    }
}
/*

@Preview(showBackground = true)
@Composable
fun EspacioScreenPreview() {
    ReservaEspaciosTheme {
        EspacioScreenContent(
            reservas = listOf(
                Reserva(1, "Sala 01", "2024-01-01", "10:00", "Reunión", "Aprobada"),
                Reserva(2, "Sala 02", "2024-01-01", "11:00", "Estudio", "Pendiente")
            ),
            onNavigateToSolicitarReserva = {}
        )
    }
}

*/



