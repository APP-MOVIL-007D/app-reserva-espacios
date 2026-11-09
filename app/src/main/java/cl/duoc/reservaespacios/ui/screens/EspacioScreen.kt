package cl.duoc.reservaespacios.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.reservaespacios.model.Espacio
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun EspacioScreen(
    navController: NavController,
    reservasViewModel: ReservasViewModel,
    modifier: Modifier = Modifier
) {
    EspacioScreenContent(
        espacios = reservasViewModel.espacios,
        onNavigateToSolicitarReserva = { espacioNombre ->
            navController.navigate("calendarioEspacio/$espacioNombre")
        },
        modifier = modifier
    )
}

@Composable
fun EspacioScreenContent(
    espacios: List<Espacio>,
    onNavigateToSolicitarReserva: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var buscarEspacio by remember { mutableStateOf("") }
    var capacidadMinimaFilter by remember { mutableIntStateOf(0) }

    // Opciones de capacidad rápida para filtrar
    val opcionesCapacidad = listOf(0, 10, 20, 30, 40)

    val espaciosFiltrados = espacios.filter { espacio ->
        val coincideTexto = espacio.nombre.contains(buscarEspacio, ignoreCase = true) ||
                espacio.equipamiento.contains(buscarEspacio, ignoreCase = true) ||
                espacio.ubicacion.contains(buscarEspacio, ignoreCase = true)

        val coincideCapacidad = espacio.capacidad >= capacidadMinimaFilter

        coincideTexto && coincideCapacidad
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Espacios",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscarEspacio,
            onValueChange = { buscarEspacio = it },
            label = { Text("Buscar espacio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Bloque/Filtro interactivo por capacidad
        Text(
            text = "Capacidad mínima de personas:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(opcionesCapacidad) { cap ->
                val esSeleccionado = capacidadMinimaFilter == cap
                FilterChip(
                    selected = esSeleccionado,
                    onClick = {
                        // Si vuelve a presionar el mismo chip, des-selecciona y vuelve a 'Todas' (0)
                        capacidadMinimaFilter = if (esSeleccionado && cap != 0) 0 else cap
                    },
                    label = {
                        Text(if (cap == 0) "Todas" else "≥ $cap pers.")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Espacios encontrados: ${espaciosFiltrados.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(espaciosFiltrados) { espacio ->
                val fotoBitmap = remember(espacio.foto) {
                    if (espacio.foto.isNotEmpty()) {
                        try {
                            BitmapFactory.decodeFile(espacio.foto)
                        } catch (_: Exception) {
                            null
                        }
                    } else null
                }

                Card(
                    onClick = { onNavigateToSolicitarReserva(espacio.nombre) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (fotoBitmap != null) {
                            Image(
                                bitmap = fotoBitmap.asImageBitmap(),
                                contentDescription = "Foto de ${espacio.nombre}",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = espacio.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${espacio.capacidad} personas · ${espacio.equipamiento.ifEmpty { espacio.ubicacion }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Ver ${espacio.nombre}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
