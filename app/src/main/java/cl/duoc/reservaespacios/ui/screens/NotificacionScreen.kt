package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import cl.duoc.reservaespacios.model.reservasDePrueba

data class Notificacion(
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val leida: Boolean
)

@Composable
fun NotificacionScreen(navController: NavController, modifier: Modifier) {

    val notificaciones = reservasDePrueba
        .filter { it.estado == "Aprobada" || it.estado == "Rechazada" }
        .map { reserva ->
            val titulo = if (reserva.estado == "Aprobada") "Reserva Aprobada" else "Reserva Rechazada"
            val mensaje = "Su reserva para ${reserva.espacio} (${reserva.horario}) ha sido ${reserva.estado.lowercase()}."
            Notificacion(
                titulo = titulo,
                mensaje = mensaje,
                fecha = reserva.fecha,
                leida = true
            )
        }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Notificaciones", style = MaterialTheme.typography.headlineMedium
        )

        if (notificaciones.isEmpty()) {
            Text("No hay notificaciones", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notificaciones) { notificacion ->
                    NotificacionCard(notificacion = notificacion)
                }
            }
        }
    }
}

@Composable
fun NotificacionCard(notificacion: Notificacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Icono notificación",
                tint = if (notificacion.leida) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notificacion.titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificacion.mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (notificacion.leida) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = notificacion.fecha,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}