package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.ui.componentes.CuadroFecha
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun InicioScreen(
    reservasViewModel: ReservasViewModel,
    menuViewModel: MenuViewModel,
    modifier: Modifier = Modifier,
    onVerEspacio: (String) -> Unit = {}
) {
    // para el recuerdo, me demore 3 horas en reescribir el inicioscreen gracias ctrl+alt+l por existir
    val proxima = reservasViewModel.proximaReserva
    val styleTitleLarge = MaterialTheme.typography.titleLarge
    val styleTitleMedium = MaterialTheme.typography.titleMedium
    val nombre = reservasViewModel.nombreUsuario

    LaunchedEffect(reservasViewModel.usuarioActual?.foto){
        val fotoGuardada = reservasViewModel.usuarioActual?.foto
        if(!fotoGuardada.isNullOrEmpty() && menuViewModel.fotoBitmap == null){
            val imagen = menuViewModel.deTextoImagen(fotoGuardada)
            if(imagen!=null) menuViewModel.actualizarFoto(imagen)
        }
    }

    //espacios disponoibles q vienen de room
    val disponibles = reservasViewModel.espacios.filter { it.estado == "Disponible" }.take(3)

    Box(modifier.fillMaxSize()) {
        //Este es el encabezado amarillo q hay en elmockup
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .statusBarsPadding()
                .height(140.dp)
        )
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val foto = menuViewModel.fotoBitmap
                        if(foto!=null){
                            Image(
                                bitmap = foto.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }else{
                            Text(
                                reservasViewModel.inicialesUsuario,
                                style = styleTitleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                //saludo
                Column {
                    Text(
                        if (nombre.isEmpty()) "¡Hola!" else "¡Hola $nombre!",
                        style = styleTitleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        reservasViewModel.rolUsuario,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    onClick = { reservasViewModel.pestanaActual = "Notificaciones" },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Notificaciones",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //tarjeta de la prox reserva, queda la mitad sobre el amairllo
            //contenido de abajo, va adentro de la Column grande para q quede debajo del encabezado
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("TU PRÓXIMA RESERVA", style = MaterialTheme.typography.labelMedium)
                    if (proxima != null) {

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CuadroFecha(proxima.fecha)

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(proxima.espacio, style = styleTitleMedium)
                                Text(
                                    "${proxima.horario} · ${proxima.motivo}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Surface(color = Color(0xFFD0E6C0), shape = RoundedCornerShape(50)) {
                                Text(
                                    "Aprobada",
                                    color = Color(0xFF31570E),
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "No tienes reservas aprobadas",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // el 'ver todos' y disponibles hoy
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Disponible hoy", style = styleTitleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Ver todos", style = MaterialTheme.typography.bodyMedium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { reservasViewModel.pestanaActual = "Espacios" }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //eran 3 nomas
            disponibles.forEach { espacio ->
                Card(
                    onClick = { onVerEspacio(espacio.nombre) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //opacidad . acá iría la foto del espacio
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(espacio.nombre, style = styleTitleMedium)
                            Text(
                                "${espacio.capacidad} personas · ${espacio.equipamiento}",
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
