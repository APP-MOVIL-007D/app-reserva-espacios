package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

fun horaStringAMinutos(hora: String): Int {
    val partes = hora.trim().split(":")
    if (partes.size == 2) {
        return (partes[0].toIntOrNull() ?: 0) * 60 + (partes[1].toIntOrNull() ?: 0)
    }
    return 0
}

data class DiaSemana(val nombre: String, val numero: Int, val fechaCompleta: String, val esPasado: Boolean = false)
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
        onSolicitarReserva = { fecha, horaInicio -> 
            val fechaEncoded = fecha.replace("/", "-")
            navController.navigate("solicitarReserva/$espacioNombre/$fechaEncoded/$horaInicio") 
        }
    )
}

@Composable
fun CalendarioEspacioScreenContent(
    espacioNombre: String,
    reservas: List<Reserva>,
    onBackClick: () -> Unit,
    onSolicitarReserva: (String, String) -> Unit
) {
    // .getInstance() va obtener la fecha actual del dispostivo.
    val today = Calendar.getInstance()
    val todayDiaStr = today.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    val todayMesStr = (today.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val todayAnioStr = today.get(Calendar.YEAR).toString()
    val todayFechaStr = "$todayDiaStr/$todayMesStr/$todayAnioStr"

    var fechaSeleccionada by remember { mutableStateOf(todayFechaStr) }
    var mesActualOffset by remember { mutableIntStateOf(0) }

    val calendario = Calendar.getInstance().apply {
        add(Calendar.MONTH, mesActualOffset)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val locCl = Locale.forLanguageTag("es-CL")
    val mesFormat = SimpleDateFormat("MMMM yyyy", locCl)
    val textoMesAnio = mesFormat.format(calendario.time).replaceFirstChar { it.uppercase() }

    val diasDelMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)
    val anioActual = calendario.get(Calendar.YEAR)
    val mesActual = calendario.get(Calendar.MONTH) + 1

    val diasBase = (1..diasDelMes).map { dia ->
        val calDia = Calendar.getInstance().apply {
            set(Calendar.YEAR, anioActual)
            set(Calendar.MONTH, mesActual - 1)
            set(Calendar.DAY_OF_MONTH, dia)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val calToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val esPasado = calDia.timeInMillis < calToday.timeInMillis
        
        val diaFormat = SimpleDateFormat("EEE", locCl)
        val nombre = diaFormat.format(calDia.time).replaceFirstChar { it.uppercase() }.take(3).replace(".", "")
        val diaStr = dia.toString().padStart(2, '0')
        val mesStr = mesActual.toString().padStart(2, '0')
        val anioStr = anioActual.toString()
        DiaSemana(nombre, dia, "$diaStr/$mesStr/$anioStr", esPasado)
    }

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

        Spacer(modifier = Modifier.height(16.dp))

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textoMesAnio,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row {
                        IconButton(
                            onClick = { mesActualOffset-- },
                            enabled = mesActualOffset > 0
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atras")
                        }
                        IconButton(
                            onClick = { mesActualOffset++ },
                            enabled = mesActualOffset < 12
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Adelante")
                        }
                    }
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    for (dia in diasBase) {
                        val esSeleccionado = dia.fechaCompleta == fechaSeleccionada
                        
                        val colorTextoBase = if (dia.esPasado) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        val colorNumeroBase = if (dia.esPasado) Color.Gray else MaterialTheme.colorScheme.onSurface

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(enabled = !dia.esPasado) { fechaSeleccionada = dia.fechaCompleta }
                        ) {
                            Text(
                                text = dia.nombre,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (esSeleccionado) MaterialTheme.colorScheme.onSurface else colorTextoBase
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
                                    color = if (esSeleccionado) MaterialTheme.colorScheme.onSecondary else colorNumeroBase
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
            val fechaVisual = diaActual.fechaCompleta.substringBeforeLast("/")
            Text("${diaActual.nombre} ${diaActual.numero} ($fechaVisual)", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(bloquesBase) { bloque ->
                
                val bloqueInicioMin = horaStringAMinutos(bloque.horaInicio)
                val bloqueFinMin = horaStringAMinutos(bloque.horaFin)

                val reservaEncontrada = reservas.find { r ->
                    if (r.espacio == espacioNombre && r.fecha == fechaSeleccionada && (r.estado == "Pendiente" || r.estado == "Aprobada")) {
                        val partes = r.horario.split("-").map { it.trim() }
                        if (partes.size == 2) {
                            val resInicioMin = horaStringAMinutos(partes[0])
                            val resFinMin = horaStringAMinutos(partes[1])
                            

                            resInicioMin < bloqueFinMin && resFinMin > bloqueInicioMin
                        } else {
                            r.horario == "${bloque.horaInicio} - ${bloque.horaFin}"
                        }
                    } else {
                        false
                    }
                }

                val estadoBloque = when {
                    reservaEncontrada != null -> reservaEncontrada.estado
                    else -> "Disponible"
                }

                val tituloBoton = when (estadoBloque) {
                    "Aprobada" -> "Reservado"
                    "Pendiente" -> "Pendiente"
                    else -> "Disponible"
                }

                val subtitulo = when (estadoBloque) {
                    "Aprobada" -> "No disponible"
                    "Pendiente" -> "Esperando aprobación"
                    else -> "Toca para solicitar"
                }

                val colorFondo = when (estadoBloque) {
                    "Aprobada" -> MaterialTheme.colorScheme.onSurface
                    "Pendiente" -> MaterialTheme.colorScheme.secondaryContainer
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
                                onSolicitarReserva(fechaSeleccionada, bloque.horaInicio)
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

/*

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

 */