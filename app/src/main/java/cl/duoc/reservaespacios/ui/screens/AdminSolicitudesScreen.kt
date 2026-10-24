package cl.duoc.reservaespacios.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun AdminSolicitudesScreen(reservasViewModel: ReservasViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val pendientes = reservasViewModel.reservas.filter { it.estado == "Pendiente" }
    val revisadas = reservasViewModel.reservas.count { it.estado == "Aprobada" || it.estado == "Rechazada" }

    Column(modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Solicitudes",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CuadroNumero(pendientes.size, "Pendientes", Modifier.weight(1f))
                    CuadroNumero(revisadas, "Revisadas", Modifier.weight(1f))
                }
            }

            item {
                Text(
                    "Solicitudes pendientes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (pendientes.isEmpty()) {
                item {
                    Text(
                        "No hay solicitudes pendientes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(pendientes, key = { it.id }) { reserva ->
                TarjetaSolicitud(
                    reserva = reserva,
                    reservasViewModel = reservasViewModel,
                    onAprobar = {
                        reservasViewModel.aprobar(reserva)
                        Toast.makeText(context, "Reserva aprobada", Toast.LENGTH_SHORT).show()
                    },
                    onRechazar = {
                        reservasViewModel.rechazar(reserva)
                        Toast.makeText(context, "Reserva rechazada", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun CuadroNumero(numero: Int, texto: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("$numero", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TarjetaSolicitud(
    reserva: Reserva,
    reservasViewModel: ReservasViewModel,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario = reservasViewModel.usuarioPorCorreo(reserva.correoUsuario)
    val nombre = if (usuario != null) "${usuario.nombre} ${usuario.apellido}" else "Sin solicitante"
    val iniciales = if (usuario != null) (usuario.nombre.take(1) + usuario.apellido.take(1)).uppercase() else "?"
    val enEspera = reservasViewModel.listaDeEspera(reserva)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(iniciales, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(nombre, style = MaterialTheme.typography.titleSmall)
                    Text(
                        usuario?.rol ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (enEspera > 0) {
                    Surface(color = Color(0xFFF4C8BB), shape = RoundedCornerShape(50)) {
                        Text(
                            "Lista de espera: $enEspera",
                            color = Color(0xFF802F1A),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(reserva.espacio, style = MaterialTheme.typography.titleMedium)
            Text(
                "${reserva.fecha} · ${reserva.horario}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                reserva.motivo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAprobar, modifier = Modifier.weight(1f)) {
                    Text("APROBAR")
                }
                OutlinedButton(onClick = onRechazar, modifier = Modifier.weight(1f)) {
                    Text("RECHAZAR")
                }
            }
        }
    }
}
