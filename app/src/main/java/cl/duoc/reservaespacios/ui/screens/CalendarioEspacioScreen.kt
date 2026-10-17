package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel

import cl.duoc.reservaespacios.model.Reserva
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.reservaespacios.ui.theme.ReservaEspaciosTheme

data class DiaSemana(val nombre: String, val numero: Int, val fechaCompleta: String)
data class Bloque(val horaInicio: String, val horaFin: String)

@Composable
fun CalendarioEspacioScreen(
    navController: NavController,
    reservasViewModel: ReservasViewModel,
    espacioNombre: String
) {
    CalendarioEspacioScreenContent(
        espacioNombre = espacioNombre,
        reservas = reservasViewModel.reservas,
        onBackClick = { navController.popBackStack() },
        onSolicitarReserva = { navController.navigate("solicitarReserva/$espacioNombre") }
    )
}

@Composable
fun CalendarioEspacioScreenContent(
    espacioNombre: String,
    reservas: List<Reserva>,
    onBackClick: () -> Unit,
    onSolicitarReserva: () -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf("2026-10-20") }
    
    val diasBase = listOf(
        DiaSemana("Lun", 19, "2026-10-19"),
        DiaSemana("Mar", 20, "2026-10-20"),
        DiaSemana("Mié", 21, "2026-10-21"),
        DiaSemana("Jue", 22, "2026-10-22"),
        DiaSemana("Vie", 23, "2026-10-23")
    )

    val bloquesBase = listOf(
        Bloque("08:31", "09:50"),
        Bloque("10:01", "11:20"),
        Bloque("11:31", "12:50"),
        Bloque("13:01", "14:20"),
        Bloque("14:31", "15:50")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = espacioNombre,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Mes y controles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Octubre 2026",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atras")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Adelante")
                        }
                    }
                }
                
                // Línea separadora usando el color secundario del tema
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dia in diasBase) {
                        val esSeleccionado = dia.fechaCompleta == fechaSeleccionada
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { fechaSeleccionada = dia.fechaCompleta }
                        ) {
                            Text(
                                text = dia.nombre,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (esSeleccionado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (esSeleccionado) MaterialTheme.colorScheme.secondary else Color.Transparent)
                            ) {
                                Text(
                                    text = dia.numero.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (esSeleccionado) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        val diaActual = diasBase.find { it.fechaCompleta == fechaSeleccionada }
        if (diaActual != null) {
            Text("${diaActual.nombre} ${diaActual.numero}", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(bloquesBase) { bloque ->
                
                val reservaEncontrada = reservas.find {
                    it.espacio == espacioNombre && 
                    it.fecha == fechaSeleccionada && 
                    (it.estado == "Pendiente" || it.estado == "Aprobada")
                }

                val estadoBloque = when {
                    bloque.horaInicio == "08:31" -> "Clase"
                    reservaEncontrada != null -> reservaEncontrada.estado
                    else -> "Disponible"
                }

                val tituloBoton = when (estadoBloque) {
                    "Aprobada" -> "Reservado"
                    "Pendiente" -> "Pendiente"
                    "Clase" -> "Clase"
                    else -> "Disponible"
                }

                val subtitulo = when (estadoBloque) {
                    "Aprobada" -> "No disponible"
                    "Pendiente" -> "Esperando aprobación"
                    "Clase" -> "Bloqueado por horario académico"
                    else -> "Toca para solicitar"
                }

                val colorFondo = when (estadoBloque) {
                    "Aprobada" -> MaterialTheme.colorScheme.onSurface
                    "Pendiente" -> MaterialTheme.colorScheme.secondaryContainer
                    "Clase" -> MaterialTheme.colorScheme.surfaceVariant
                    else -> MaterialTheme.colorScheme.surface
                }
                
                val colorBorde = when (estadoBloque) {
                    "Disponible" -> MaterialTheme.colorScheme.primary
                    "Pendiente" -> MaterialTheme.colorScheme.secondary
                    else -> Color.Transparent
                }

                val colorTexto = when (estadoBloque) {
                    "Aprobada" -> MaterialTheme.colorScheme.surface
                    else -> MaterialTheme.colorScheme.onSurface
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(50.dp)) {
                        Text(bloque.horaInicio, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(bloque.horaFin, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .border(1.dp, colorBorde, RoundedCornerShape(8.dp))
                            .background(colorFondo, RoundedCornerShape(8.dp))
                            .clickable(enabled = estadoBloque == "Disponible") {
                                onSolicitarReserva()
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = tituloBoton, 
                                style = MaterialTheme.typography.bodyLarge, 
                                fontWeight = FontWeight.Bold, 
                                color = colorTexto
                            )
                            Text(
                                text = subtitulo, 
                                style = MaterialTheme.typography.bodySmall, 
                                color = if (estadoBloque == "Disponible") MaterialTheme.colorScheme.primary else colorTexto.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarioEspacioScreenPreview() {
    ReservaEspaciosTheme {
        CalendarioEspacioScreenContent(
            espacioNombre = "Sala 1",
            reservas = listOf(
                Reserva(
                    id = 1,
                    espacio = "Sala 1",
                    fecha = "2026-10-20",
                    horario = "08:31 - 09:50",
                    motivo = "Clase",
                    estado = "Pendiente"
                ),
                Reserva(
                    id = 2,
                    espacio = "Sala 1",
                    fecha = "2026-10-20",
                    horario = "10:01 - 11:20",
                    motivo = "Reunión",
                    estado = "Aprobada"
                )
            ),
            onBackClick = {},
            onSolicitarReserva = {}
        )
    }
}