package cl.duoc.reservaespacios.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun InicioScreen(reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier) {
    val proxima = reservasViewModel.reservas.firstOrNull { it.estado == "Aprobada" }

    val styleTitleLarge = MaterialTheme.typography.titleLarge
    val styleTitleMedium = MaterialTheme.typography.titleMedium

    val meses =
        listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")

    val nombre = reservasViewModel.nombreUsuario

    Column(modifier.fillMaxSize()) {
        //Este es el encabezado amarillo q hay en elmockup
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        reservasViewModel.inicialesUsuario,
                        style = styleTitleMedium,
                        fontWeight = FontWeight.Bold
                    )
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
                Text(reservasViewModel.rolUsuario, color = MaterialTheme.colorScheme.onSecondary)
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

        //contenido de abajo, va adentro de la Column grande para q quede debajo del encabezado
        Card(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("TU PRÓXIMA RESERVA", style = MaterialTheme.typography.labelMedium)
                if (proxima != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val dia = proxima.fecha.substringBefore("/")
                    val numeroMes = proxima.fecha.substringAfter("/").toIntOrNull() ?: 1
                    val mes = meses.getOrElse(numeroMes - 1) { "" }

                    Row(verticalAlignment = Alignment.CenterVertically){
                        Surface(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(56.dp)
                        ){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(dia, style = styleTitleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
                                Text(mes, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)){
                            Text(proxima.espacio, style = styleTitleMedium)
                            Text("${proxima.horario} - ${proxima.motivo}", style = MaterialTheme.typography.bodySmall)
                        }

                        Surface(color = Color(0xFFD0E6C0), shape = RoundedCornerShape(50)){
                            Text("Aprobada",
                                color = Color(0xFF31570E),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }

                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("No tienes reservas aprobadas", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
