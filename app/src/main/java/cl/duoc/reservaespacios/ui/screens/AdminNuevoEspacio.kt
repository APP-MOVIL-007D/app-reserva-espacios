package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.reservaespacios.ui.theme.*
import cl.duoc.reservaespacios.utils.Validaciones
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun AdminNuevoEspacio(
    viewModel: ReservasViewModel,
    onEspacioGuardado: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var equipamiento by remember { mutableStateOf("") }
    var estadoInicial by remember { mutableStateOf("Disponible") }
    var mostrarValidacion by remember { mutableStateOf(true) } // Activo para mostrar el estado del mockup

    val errorNombre = if (mostrarValidacion) Validaciones.validarNombreEspacio(nombre) else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuocBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DuocBlack)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VISTA ADMINISTRADOR",
                color = DuocYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.weight(1f))
                Text("Nuevo espacio", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1.2f))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Información del espacio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "Completa los datos para agregar un espacio a la sede.",
                fontSize = 13.sp,
                color = DuocGrayText
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    mostrarValidacion = true
                },
                label = { Text("Nombre") },
                placeholder = { Text("Ej: Sala colaborativa") },
                singleLine = true,
                isError = errorNombre != null,
                trailingIcon = {
                    if (errorNombre != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = RedError)
                    }
                },
                supportingText = {
                    if (errorNombre != null) {
                        Text(errorNombre, color = RedError)
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = capacidad,
                onValueChange = { capacidad = it },
                label = { Text("Capacidad") },
                placeholder = { Text("Número de personas") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = equipamiento,
                onValueChange = { equipamiento = it },
                label = { Text("Equipamiento") },
                placeholder = { Text("Describe el equipamiento disponible") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = estadoInicial,
                onValueChange = { estadoInicial = it },
                label = { Text("Estado inicial") },
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del espacio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DuocBorderGray, RoundedCornerShape(12.dp))
                    .background(DuocSurface, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = DuocGrayText)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Imagen del espacio", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("Tomar foto o elegir de la galería", fontSize = 12.sp, color = DuocGrayText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    mostrarValidacion = true
                    if (nombre.isNotBlank()) {
                        viewModel.agregarEspacio(
                            nombre = nombre,
                            capacidad = capacidad.toIntOrNull() ?: 30,
                            equipamiento = equipamiento,
                            disponible = true
                        )
                        nombre = ""
                        capacidad = ""
                        equipamiento = ""
                        onEspacioGuardado()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DuocBlack),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("GUARDAR", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}