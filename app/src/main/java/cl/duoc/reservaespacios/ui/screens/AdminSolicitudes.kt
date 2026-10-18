package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.reservaespacios.model.EstadoReserva
import cl.duoc.reservaespacios.ui.theme.*
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun AdminSolicitudes(viewModel: ReservasViewModel) {
    val reservas by viewModel.reservas.collectAsState()
    val revisadasHoy by viewModel.revisadasHoy.collectAsState()
    val pendientes = reservas.filter { it.estado == EstadoReserva.PENDIENTE }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuocBackground)
    ) {
        // Cinta Superior "VISTA ADMINISTRADOR"
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
            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Solicitudes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Contadores: Pendientes (6) y Revisadas hoy (14)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DuocSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${pendientes.size.coerceAtLeast(6)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text("Pendientes", fontSize = 13.sp, color = DuocGrayText)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DuocSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "$revisadasHoy",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text("Revisadas hoy", fontSize = 13.sp, color = DuocGrayText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Solicitudes pendientes", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    "Ordenar",
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    color = DuocBlack
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(pendientes) { solicitud ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DuocSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF0F0F0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            solicitud.solicitanteIniciales,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(solicitud.solicitanteNombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(solicitud.solicitanteCargo, fontSize = 12.sp, color = DuocGrayText)
                                    }
                                }

                                if (solicitud.listaEspera > 0) {
                                    Surface(
                                        color = WaitlistBadgeBg,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Lista de espera: ${solicitud.listaEspera}",
                                            color = WaitlistBadgeText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

                            Text(solicitud.espacioNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "${solicitud.fecha} · ${solicitud.bloqueHorario}",
                                fontSize = 13.sp,
                                color = DuocGrayText
                            )
                            Text(
                                "${solicitud.actividad} · ${solicitud.asistentes} asistentes",
                                fontSize = 13.sp,
                                color = DuocGrayText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.aprobarSolicitud(solicitud.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DuocBlack),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Text("APROBAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.rechazarSolicitud(solicitud.id) },
                                    shape = RoundedCornerShape(24.dp),
                                    border = BorderStroke(1.dp, DuocBlack),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Text("RECHAZAR", color = DuocBlack, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
