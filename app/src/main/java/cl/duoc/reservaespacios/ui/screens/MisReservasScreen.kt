package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun MisReservasScreen(reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier) {

    val pestanasReserva = listOf("Pendientes", "Aprobadas", "Historial")
    var pestanaActual by remember { mutableIntStateOf(0) }
    var reservaACancelar by remember { mutableStateOf<Reserva?> (null) }

    val estadoBuscado = when (pestanaActual) {
        0 -> "Pendiente"
        1 -> "Aprobada"
        else -> "Rechazada"
    }

    val reservasFiltradas = reservasViewModel.reservas.filter {
        if (pestanaActual == 2) it.estado == "Rechazada" || it.estado == "Cancelada"
        else it.estado == estadoBuscado
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis reservas", style = MaterialTheme.typography.headlineMedium)


        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTabRow(selectedTabIndex = pestanaActual) {
            pestanasReserva.forEachIndexed { index, nombre ->
                Tab(
                    selected = pestanaActual == index,
                    onClick = { pestanaActual = index },
                    text = { Text(nombre) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (reservasFiltradas.isEmpty()) {
            Text(
                "No tienes reservas en esta sección",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn {
                items(reservasFiltradas, key = {it.id}) { reserva ->

                    val colorFondo = when (reserva.estado) {
                        "Aprobada" -> Color(0xFFD0E6C0)
                        "Rechazada" -> Color(0xFFF4C8BB)
                        "Cancelada" -> Color(0xFF5F6368)
                        else -> Color(0xFFFFF1CC)
                    }

                    val colorTexto = when (reserva.estado) {
                        "Aprobada" -> Color(0xFF31570E)
                        "Rechazada" -> Color(0xFF802F1A)
                        "Cancelada" -> Color.White
                        else -> Color(0xFF8A5A00)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .animateItem()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(color = colorFondo, shape = RoundedCornerShape(50)) {
                                Text(
                                    reserva.estado,
                                    color = colorTexto,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(reserva.espacio,
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration =
                                    if(reserva.estado == "Cancelada") TextDecoration.LineThrough else null
                                )
                            Text("${reserva.fecha} · ${reserva.horario}")
                            Text(reserva.motivo, style = MaterialTheme.typography.bodyMedium)
                            if (reserva.estado == "Pendiente" || reserva.estado == "Aprobada") {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { reservaACancelar = reserva},
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Cancelar")
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    reservaACancelar?.let { reserva: Reserva ->
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Seguro que quieres cancelar la reserva de ${reserva.espacio} el ${reserva.fecha}?") },
            confirmButton = {
                TextButton(onClick = {
                    reservasViewModel.cancelar(reserva)
                    reservaACancelar = null
                }) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(onClick = { reservaACancelar = null }) { Text("No") }
            }
        )
    }


}