package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.reservaespacios.ui.theme.*
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNuevoEspacio(
    viewModel: ReservasViewModel,
    onEspacioGuardado: () -> Unit,
    onVolver: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var equipamiento by remember { mutableStateOf("") }
    var estadoSeleccionado by remember { mutableStateOf("Disponible") }
    var menuExpandido by remember { mutableStateOf(false) }
    var mostrarValidacion by remember { mutableStateOf(true) } // Para reflejar el estado del mockup

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TarjetaClara)
            .verticalScroll(scrollState)
    ) {
        // Barra superior VISTA ADMINISTRADOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NegroDuoc)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VISTA ADMINISTRADOR",
                color = AmarilloDuoc,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Header con flecha y título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = NegroDuoc
                )
            }
            Text(
                text = "Nuevo espacio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NegroDuoc,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Información del espacio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NegroDuoc
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Completa los datos para agregar un espacio a la sede.",
                fontSize = 14.sp,
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Campo Nombre (con error según mockup)
            val esErrorNombre = mostrarValidacion && nombre.isBlank()
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                placeholder = { Text("Ej: Sala colaborativa") },
                isError = esErrorNombre,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NegroDuoc,
                    unfocusedBorderColor = BordeGris,
                    errorBorderColor = RojoError,
                    errorLabelColor = RojoError
                ),
                singleLine = true
            )
            if (esErrorNombre) {
                Text(
                    text = "El nombre es obligatorio",
                    color = RojoError,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Capacidad
            OutlinedTextField(
                value = capacidad,
                onValueChange = { capacidad = it },
                label = { Text("Capacidad") },
                placeholder = { Text("Número de personas") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NegroDuoc,
                    unfocusedBorderColor = BordeGris
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Equipamiento
            OutlinedTextField(
                value = equipamiento,
                onValueChange = { equipamiento = it },
                label = { Text("Equipamiento") },
                placeholder = { Text("Describe el equipamiento disponible") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NegroDuoc,
                    unfocusedBorderColor = BordeGris
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdown Estado Inicial
            ExposedDropdownMenuBox(
                expanded = menuExpandido,
                onExpandedChange = { menuExpandido = !menuExpandido }
            ) {
                OutlinedTextField(
                    value = estadoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado inicial") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NegroDuoc,
                        unfocusedBorderColor = BordeGris
                    )
                )
                ExposedDropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Disponible") },
                        onClick = { estadoSeleccionado = "Disponible"; menuExpandido = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Mantenimiento") },
                        onClick = { estadoSeleccionado = "Mantenimiento"; menuExpandido = false }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recuadro punteado: Imagen del espacio
            val yellowStroke = AmarilloDuoc
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 3f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                        )
                        drawRoundRect(
                            color = yellowStroke,
                            cornerRadius = CornerRadius(16.dp.toPx()),
                            style = stroke
                        )
                    }
                    .clickable { /* Abrir cámara o galería */ },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📷",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "Imagen del espacio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NegroDuoc
                        )
                        Text(
                            text = "Tomar foto o elegir de la galería",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón GUARDAR
            Button(
                onClick = {
                    mostrarValidacion = true
                    if (nombre.isNotBlank()) {
                        viewModel.agregarEspacio(
                            nombre = nombre,
                            capacidad = capacidad.toIntOrNull() ?: 30,
                            equipamiento = equipamiento,
                            disponible = (estadoSeleccionado == "Disponible")
                        )
                        onEspacioGuardado()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NegroDuoc),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "GUARDAR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}