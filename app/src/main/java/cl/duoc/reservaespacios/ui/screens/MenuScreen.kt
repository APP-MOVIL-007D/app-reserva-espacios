package cl.duoc.reservaespacios.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.reservaespacios.ui.viewmodel.MenuEvento
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel


@Composable
fun MenuScreen(navController: NavController, viewModel: MenuViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var mostrarDialogoFoto by remember { mutableStateOf(false) }
    var bitmapImagen by remember { mutableStateOf<Bitmap?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
            if (bitmap != null) {
                bitmapImagen = bitmap
            }
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            bitmapImagen = it
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventos.collect { evento ->
            when (evento) {
                is MenuEvento.Mensaje ->
                    Toast.makeText(context, evento.texto, Toast.LENGTH_SHORT).show()
                MenuEvento.SesionCerrada -> {
                    Toast.makeText(context, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                }
            }
        }
    }

    if (uiState.mostrarDialogoSalir) {
        AlertDialog(
            onDismissRequest = viewModel::cancelarCerrarSesion,
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarCerrarSesion) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelarCerrarSesion) { Text("No") }
            }
        )
    }

    if (mostrarDialogoFoto) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoFoto = false },
            title = { Text("Actualizar foto de perfil") },
            text = { Text("Elige una opción para tu nueva foto de perfil:") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoFoto = false
                    camaraLauncher.launch(null)
                }) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoFoto = false
                    galeriaLauncher.launch("image/*")
                }) {
                    Text("Galería")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 80.dp)
        )

        Surface(
            modifier = Modifier
                .size(120.dp)
                .clickable { mostrarDialogoFoto = true },
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            if (bitmapImagen != null) {
                Image(
                    bitmap = bitmapImagen!!.asImageBitmap(),
                    contentDescription = "foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "foto de perfil",
                    modifier = Modifier.padding(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = uiState.nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiState.correo
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::pedirCerrarSesion
        ) {
            Text("Cerrar sesión")
        }
    }
}