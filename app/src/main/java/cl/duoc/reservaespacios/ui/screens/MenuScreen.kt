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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.ui.theme.Rojo
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    reservasViewModel: ReservasViewModel,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    //lambda
    onCerrarSesion: () -> Unit,
    onIrASolicitudes: () -> Unit,
    onIrACargarHorario: () -> Unit,
    onIrANuevoEspacio: () -> Unit,
    onIrAMisReservas: () -> Unit,
    onIrANotificaciones: () -> Unit
) {
    val styleTitleLarge = MaterialTheme.typography.titleLarge
    val styleTitleMedium = MaterialTheme.typography.titleMedium

    val nombre = reservasViewModel.nombreUsuario
    val correo = reservasViewModel.correoUsuario
    val rol = reservasViewModel.rolUsuario

    val context = LocalContext.current

    var rojo = Rojo
    var gris = Color(0xFF949891)


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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Menu",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

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
        Text(
            text = "$rol: $correo",
            color = gris
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            //Secciones Botones
            if (reservasViewModel.esAdmin) {

                Encabezado("ADMINISTRACIÓN")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ){
                    BloqueBoton(
                        icon = Icons.Default.AppRegistration,
                        title = "Solicitudes",
                        onClick = onIrASolicitudes
                    )

                    BloqueBoton(
                        icon = Icons.Default.Unarchive,
                        title = "Cargar Horario",
                        onClick = onIrACargarHorario
                    )

                    BloqueBoton(
                        icon = Icons.Default.BorderColor,
                        title = "Nuevo Espacio",
                        onClick = onIrANuevoEspacio
                    )
                }

            } else {
                Encabezado("MI CUENTA")
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ){
                    BloqueBoton(
                        icon = Icons.Default.DateRange,
                        title = "Mis reservas",
                        onClick = onIrAMisReservas
                    )
                    BloqueBoton(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones",
                        onClick = onIrANotificaciones
                    )
                }

            }
            Encabezado("CONFIGURACIÓN")
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ){
                BloqueBoton(
                    icon = Icons.Default.NightlightRound,
                    title = "Modo oscuro",
                    onClick = { reservasViewModel.cambiarModoOscuro(!reservasViewModel.modoOscuro) },
                    trailing = {
                        Switch(
                            checked = reservasViewModel.modoOscuro,
                            onCheckedChange = { reservasViewModel.cambiarModoOscuro(it) }
                        )
                    }
                )
                BloqueBoton(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Cerrar Sesión",
                    textColor = rojo,
                    iconColor = rojo,
                    onClick = { menuViewModel.pedirCerrarSesion()  }
                )
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

@Composable
fun Encabezado(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    )
}


@Composable
fun BloqueBoton(
    icon: ImageVector,
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = Color.DarkGray,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.LightGray
        )
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        trailing()
    }
}