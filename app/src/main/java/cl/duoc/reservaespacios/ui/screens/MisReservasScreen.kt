package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.componentes.CuadroFecha
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel
import cl.duoc.reservaespacios.utils.sumarMins
import cl.duoc.reservaespacios.utils.textoDia

@Composable
fun MisReservasScreen(reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier) {

    val pestanasReserva = listOf("Pendientes", "Aprobadas", "Historial")
    var pestanaActual by remember { mutableIntStateOf(0) }
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    val estadosPestana = when (pestanaActual) {
        0 -> listOf("Pendiente")
        1 -> listOf("Aprobada", "En uso")
        else -> listOf("Rechazada", "Cancelada", "Finalizada")
    }

    val reservasFiltradas = reservasViewModel.misReservas.filter { it.estado in estadosPestana }

    Column(modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.statusBarsPadding()) {
                Text(
                    "Mis reservas",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                PrimaryTabRow(
                    selectedTabIndex = pestanaActual,
                    containerColor = MaterialTheme.colorScheme.surface,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(pestanaActual, matchContentSize = true),
                            width = Dp.Unspecified,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    divider = {}
                ) {
                    pestanasReserva.forEachIndexed { index, nombre ->
                        Tab(
                            selected = pestanaActual == index,
                            onClick = { pestanaActual = index },
                            text = { Text(nombre) },
                            selectedContentColor = MaterialTheme.colorScheme.onSurface,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (reservasFiltradas.isEmpty()) {
                Text(
                    "No tienes reservas en esta sección",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn {
                    items(reservasFiltradas, key = { it.id }) { reserva ->

                        val colorFondo = when (reserva.estado) {
                            "Aprobada" -> Color(0xFFD0E6C0)
                            "Rechazada" -> Color(0xFFF4C8BB)
                            "Cancelada" -> Color(0xFF5F6368)
                            "Finalizada" -> Color(0xFFD0E6C0)
                            else -> Color(0xFFFFF1CC)
                        }

                        val colorTexto = when (reserva.estado) {
                            "Aprobada" -> Color(0xFF31570E)
                            "Rechazada" -> Color(0xFF802F1A)
                            "Cancelada" -> Color.White
                            "Finalizada" -> Color(0xFF31570E)
                            else -> Color(0xFF8A5A00)
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .animateItem()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(color = colorFondo, shape = RoundedCornerShape(50)) {
                                        Text(
                                            reserva.estado,
                                            color = colorTexto,
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }
                                    Text(
                                        textoDia(reserva.fecha),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CuadroFecha(reserva.fecha)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            reserva.espacio,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            reserva.horario,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            if (reserva.estado == "En uso") "Ingreso registrado a las ${reserva.horaIngreso}" else
                                                reserva.motivo,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                val esHoy = textoDia(reserva.fecha) == "Hoy"

                                if (reserva.estado == "Aprobada" && esHoy) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(onClick = {
                                        reservasViewModel.registrarIngreso(reserva)
                                    }, modifier = Modifier.fillMaxWidth()) {
                                        Text("REGISTRAR INGRESO")
                                    }
                                }

                                if (reserva.estado == "En uso") {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedButton(
                                        onClick = {
                                            reservasViewModel.registrarSalida(reserva)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("REGISTRAR SALIDA")
                                    }
                                }

                                if(reserva.estado == "Pendiente" || reserva.estado == "Aprobada"){
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment =  Alignment.CenterVertically){
                                        if(reserva.estado == "Aprobada" && esHoy){
                                            Text("Disponible hasta las ${sumarMins(reserva.horario.take(5), 10)}",
                                                style =MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text("Cancelar",
                                            color= MaterialTheme.colorScheme.error, textDecoration = TextDecoration.Underline,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.clickable{ reservaACancelar = reserva})
                                    }
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