@file:OptIn(ExperimentalMaterial3Api::class)

package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.viewmodel.CalendarioEspacioViewModel
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel
import cl.duoc.reservaespacios.utils.BLOQUES_HORARIOS_BASE
import java.util.Calendar
import java.util.TimeZone

@Composable
fun CalendarioEspacioScreen(
    navController: NavController,
    reservasViewModel: ReservasViewModel,
    espacioNombre: String,
    calendarioViewModel: CalendarioEspacioViewModel = viewModel()
) {
    CalendarioEspacioScreenContent(
        espacioNombre = espacioNombre,
        reservas = reservasViewModel.reservas,
        calendarioViewModel = calendarioViewModel,
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
    calendarioViewModel: CalendarioEspacioViewModel,
    onBackClick: () -> Unit,
    onSolicitarReserva: (String, String) -> Unit
) {
    val textoMesAnio = calendarioViewModel.obtenerTextoMesAnio()
    val diasBase = remember(calendarioViewModel.mesActualOffset) { calendarioViewModel.generarDiasDelMes() }
    val fechaSeleccionada = calendarioViewModel.fechaSeleccionada
    var mostrarDatePicker by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    // Auto-scroll al día seleccionado (solo si el día NO ha pasado)
    LaunchedEffect(fechaSeleccionada, calendarioViewModel.mesActualOffset) {
        val indiceSeleccionado = diasBase.indexOfFirst { it.fechaCompleta == fechaSeleccionada }
        if (indiceSeleccionado >= 0) {
            val diaSeleccionado = diasBase[indiceSeleccionado]
            if (!diaSeleccionado.esPasado) {
                lazyListState.animateScrollToItem(index = indiceSeleccionado, scrollOffset = -150)
            }
        }
    }

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val calSelected = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcTimeMillis }
                    val calToday = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    return calSelected.timeInMillis >= calToday.timeInMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        calendarioViewModel.seleccionarFechaDesdePicker(millis)
                    }
                    mostrarDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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

        // Tarjeta con Navegación de Meses e Ícono de DatePicker
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { mostrarDatePicker = true }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha"
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = { calendarioViewModel.retrocederMes() },
                            enabled = calendarioViewModel.mesActualOffset > 0
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Atrás")
                        }
                        IconButton(
                            onClick = { calendarioViewModel.avanzarMes() },
                            enabled = calendarioViewModel.mesActualOffset < 12
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Adelante")
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )

                LazyRow(
                    state = lazyListState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(diasBase) { dia ->
                        val esSeleccionado = dia.fechaCompleta == fechaSeleccionada
                        val colorTextoBase = if (dia.esPasado) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        val colorNumeroBase = if (dia.esPasado) Color.Gray else MaterialTheme.colorScheme.onSurface

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(enabled = !dia.esPasado) {
                                calendarioViewModel.cambiarFechaSeleccionada(dia.fechaCompleta)
                            }
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
            Text(
                text = "${diaActual.nombre} ${diaActual.numero} ($fechaVisual)",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(BLOQUES_HORARIOS_BASE) { bloque ->
                val estadoBloque = calendarioViewModel.obtenerEstadoBloque(bloque, espacioNombre, reservas)

                var tituloBoton = "Disponible"
                var subtitulo = "Toca para solicitar"
                var colorFondo = MaterialTheme.colorScheme.surface
                var colorBorde = MaterialTheme.colorScheme.primary
                var colorTexto = MaterialTheme.colorScheme.onSurface

                when (estadoBloque) {
                    "Aprobada" -> {
                        tituloBoton = "Reservado"
                        subtitulo = "No disponible"
                        colorFondo = MaterialTheme.colorScheme.onSurface
                        colorBorde = Color.Transparent
                        colorTexto = MaterialTheme.colorScheme.surface
                    }
                    "Pendiente" -> {
                        tituloBoton = "Pendiente"
                        subtitulo = "Esperando aprobación"
                        colorFondo = MaterialTheme.colorScheme.secondaryContainer
                        colorBorde = MaterialTheme.colorScheme.secondary
                    }
                    "Pasado" -> {
                        tituloBoton = "Expirado"
                        subtitulo = "Horario pasado"
                        colorFondo = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        colorBorde = Color.Transparent
                        colorTexto = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }
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
