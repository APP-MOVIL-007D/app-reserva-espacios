package cl.duoc.reservaespacios.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.ui.componentes.BarraAdmin
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel
import cl.duoc.reservaespacios.utils.leerFotoGaleria
import cl.duoc.reservaespacios.utils.validarCapacidad
import cl.duoc.reservaespacios.utils.validarNombreEspacio
import cl.duoc.reservaespacios.utils.validarUbicacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNuevoEspacioScreen(reservasViewModel: ReservasViewModel, onVolver: () -> Unit) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var equipamiento by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Disponible") }
    var foto by remember { mutableStateOf<Bitmap?>(null) }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCapacidad by remember { mutableStateOf<String?>(null) }
    var errorUbicacion by remember { mutableStateOf<String?>(null) }

    var menuEstado by remember { mutableStateOf(false) }
    var elegirFoto by remember { mutableStateOf(false) }

    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) foto = bitmap
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) foto = leerFotoGaleria(context, uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BarraAdmin()

        Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    "Nuevo espacio",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp)
        ) {
            Text("Información del espacio", style = MaterialTheme.typography.titleMedium)
            Text(
                "Completa los datos para agregar un espacio a la sede.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoEspacio(
                valor = nombre,
                onCambio = { nombre = it; errorNombre = null },
                etiqueta = "Nombre",
                ejemplo = "Ej: Sala colaborativa",
                error = errorNombre
            )
            CampoEspacio(
                valor = capacidad,
                onCambio = { capacidad = it; errorCapacidad = null },
                etiqueta = "Capacidad",
                ejemplo = "Número de personas",
                error = errorCapacidad,
                teclado = KeyboardType.Number
            )
            CampoEspacio(
                valor = ubicacion,
                onCambio = { ubicacion = it; errorUbicacion = null },
                etiqueta = "Ubicación",
                ejemplo = "Ej: Edificio Norte, piso 3",
                error = errorUbicacion
            )
            CampoEspacio(
                valor = equipamiento,
                onCambio = { equipamiento = it },
                etiqueta = "Equipamiento",
                ejemplo = "Describe el equipamiento disponible",
                error = null,
                unaLinea = false
            )

            ExposedDropdownMenuBox(
                expanded = menuEstado,
                onExpandedChange = { menuEstado = it }
            ) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado inicial") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEstado) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuEstado, onDismissRequest = { menuEstado = false }) {
                    listOf("Disponible", "En mantención").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                estado = opcion
                                menuEstado = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                onClick = { elegirFoto = true },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Imagen del espacio", style = MaterialTheme.typography.titleSmall)
                            Text(
                                if (foto == null) "Tomar foto o elegir de la galería" else "Toca para cambiarla",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    foto?.let { imagen ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Image(
                            bitmap = imagen.asImageBitmap(),
                            contentDescription = "Foto del espacio",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    errorNombre = validarNombreEspacio(nombre, reservasViewModel.existeEspacio(nombre))
                    errorCapacidad = validarCapacidad(capacidad)
                    errorUbicacion = validarUbicacion(ubicacion)

                    if (errorNombre == null && errorCapacidad == null && errorUbicacion == null) {
                        reservasViewModel.agregarEspacio(
                            nombre, capacidad.toInt(), equipamiento, ubicacion, estado, foto
                        )
                        Toast.makeText(context, "Espacio guardado", Toast.LENGTH_SHORT).show()
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR")
            }
        }
    }

    if (elegirFoto) {
        AlertDialog(
            onDismissRequest = { elegirFoto = false },
            title = { Text("Foto del espacio") },
            text = { Text("¿Desde dónde quieres agregar la foto?") },
            confirmButton = {
                TextButton(onClick = {
                    elegirFoto = false
                    camaraLauncher.launch(null)
                }) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(onClick = {
                    elegirFoto = false
                    galeriaLauncher.launch("image/*")
                }) { Text("Galería") }
            }
        )
    }
}

@Composable
private fun CampoEspacio(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    ejemplo: String,
    error: String?,
    teclado: KeyboardType = KeyboardType.Text,
    unaLinea: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = { Text(ejemplo) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        trailingIcon = { if (error != null) Icon(Icons.Filled.Error, contentDescription = "Error") },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        singleLine = unaLinea,
        minLines = if (unaLinea) 1 else 2,
        modifier = Modifier.fillMaxWidth()
    )
}
