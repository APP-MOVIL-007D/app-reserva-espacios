package cl.duoc.reservaespacios.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    reservasViewModel: ReservasViewModel,
    modifier: Modifier = Modifier,
    //lambda
    onCerrarSesion: () -> Unit,
    onIrASolicitudes: () -> Unit,
    onIrACargarHorario: () -> Unit,
    onIrANuevoEspacio: () -> Unit
) {
    val styleTitleLarge = MaterialTheme.typography.titleLarge
    val styleTitleMedium = MaterialTheme.typography.titleMedium

    val nombre = reservasViewModel.nombreUsuario

    val context = LocalContext.current

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    // Cargar Foto
    LaunchedEffect(reservasViewModel.usuarioActual?.foto) {
        val fotoGuardada = reservasViewModel.usuarioActual?.foto
        if (!fotoGuardada.isNullOrEmpty() && menuViewModel.fotoBitmap == null) {
            val imagenBitMap = menuViewModel.deTextoImagen(fotoGuardada)
            if (imagenBitMap != null) {
                menuViewModel.actualizarFoto(imagenBitMap)
            }
        }
    }

    // Actualizar y Guardar Foto
    val actualizarYGuardarFoto: (Bitmap?) -> Unit = { bitmap ->
        if (bitmap != null) {
            menuViewModel.actualizarFoto(bitmap)
            val imagenTexto = menuViewModel.deImagenTexto(bitmap)
            reservasViewModel.actualizarFotoUsuario(imagenTexto)
        }
    }

    // configurando acceso a galeria y camara
    val galeriaLauncher = rememberLauncherForActivityResult (
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
            actualizarYGuardarFoto(bitmap)
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        actualizarYGuardarFoto(bitmap)
    }

    // Estructura del menu
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton (
                onClick = { menuViewModel.pedirCerrarSesion() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "CerrarSesion"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Foto de Perfil con animación
        AnimatedVisibility (
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) + scaleIn(animationSpec = tween(1000))
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(100.dp)
                    .clickable { menuViewModel.pedirCambiarFoto() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (menuViewModel.fotoBitmap != null) {
                        Image(
                            bitmap = menuViewModel.fotoBitmap!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = reservasViewModel.inicialesUsuario,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombre,
            style = styleTitleMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        //Botones Admin
        if (reservasViewModel.esAdmin) {
            Card (
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button (
                        onClick = onIrASolicitudes,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Solicitudes")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onIrACargarHorario,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cargar Horario")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onIrANuevoEspacio,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Nuevo Espacio")
                    }
                }
            }
        }
    }

    // Toast
    if (menuViewModel.mostrarToastCerrarSesion) {
        ToastCerrarSesion(
            onConfirmar = { menuViewModel.confirmarCerrarSesion(onCerrarSesion) },
            onCancelar = { menuViewModel.cancelarCerrarSesion() }
        )
    }

    if (menuViewModel.mostrarToastFoto) {
        ToastFoto(
            onCamaraClick = {
                menuViewModel.procesarSeleccionCamara { camaraLauncher.launch(null) }
            },
            onGaleriaClick = {
                menuViewModel.procesarSeleccionGaleria { galeriaLauncher.launch("image/*") }
            },
            onCancelar = { menuViewModel.cancelarCambiarFoto() }
        )
    }


}

@Composable
fun ToastCerrarSesion(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Cerrar Sesión") },
        text = { Text("¿Estás seguro que quieres cerrar sesión?") },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Sí, salir", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ToastFoto(
    onCamaraClick: () -> Unit,
    onGaleriaClick: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Foto de perfil") },
        text = { Text("¿Desde dónde quieres seleccionar tu nueva foto?") },
        confirmButton = {
            TextButton(onClick = onCamaraClick) {
                Text("Cámara")
            }
        },
        dismissButton = {
            TextButton(onClick = onGaleriaClick) {
                Text("Galería")
            }
        }
    )
}