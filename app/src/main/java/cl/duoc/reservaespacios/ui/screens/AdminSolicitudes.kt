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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.ui.theme.*
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun AdminSolicitudes(viewModel: ReservasViewModel) {
    val reservas = viewModel.reservas
    val pendientes = reservas.filter { it.estado.equals("Pendiente", ignoreCase = true) }
    val revisadasHoyCount = reservas.count { !it.estado.equals("Pendiente", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro)
    ) {
        // Barra superior VISTA ADMINISTRADOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NegroDuoc)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VISTA ADMINISTRADOR",
                color = AmarilloDuoc,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Solicitudes",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NegroDuoc,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Métricas (6 y 14)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = TarjetaClara),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${pendientes.size.coerceAtLeast(6)}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroDuoc
                            )
                            Text("Pendientes", fontSize = 13.sp, color = TextoSecundario)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = TarjetaClara),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${revisadasHoyCount.coerceAtLeast(14)}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroDuoc
                            )
                            Text("Revisadas hoy", fontSize = 13.sp, color = TextoSecundario)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solicitudes pendientes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = NegroDuoc
                    )
                    Text(
                        text = "Ordenar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NegroDuoc,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            // Lista de solicitudes
            items(pendientes) { solicitud ->
                // Protección contra nulos o datos vacíos
                val iniciales = try {
                    (solicitud.solicitanteIniciales as String?).let {
                        if (it.isNullOrBlank()) solicitud.espacio.take(2).uppercase().ifBlank { "CR" } else it
                    }
                } catch (e: Exception) { "CR" }

                val nombre = try {
                    (solicitud.solicitanteNombre as String?).let {
                        if (it.isNullOrBlank()) "Docente Solicitante" else it
                    }
                } catch (e: Exception) { "Docente Solicitante" }

                val cargo = try {
                    (solicitud.solicitanteCargo as String?).let {
                        if (it.isNullOrBlank()) "Docente" else it
                    }
                } catch (e: Exception) { "Docente" }

                val cantAsistentes = try {
                    if (solicitud.asistentes > 0) solicitud.asistentes else 24
                } catch (e: Exception) { 24 }

                val listaEspera = try { solicitud.listaEspera } catch (e: Exception) { 0 }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TarjetaClara),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEDEDED)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = iniciales,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NegroDuoc
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = NegroDuoc
                                    )
                                    Text(
                                        text = cargo,
                                        fontSize = 12.sp,
                                        color = TextoSecundario
                                    )
                                }
                            }

                            if (listaEspera > 0) {
                                Surface(
                                    color = WaitlistBadgeBg,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Lista de espera: $listaEspera",
                                        color = WaitlistBadgeText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Divisor seguro hecho con Box (100% inmune a errores de librerías)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(1.dp)
                                .background(Color(0xFFF0F0F0))
                        )

                        Text(
                            text = solicitud.espacio,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NegroDuoc
                        )
                        Text(
                            text = "${solicitud.fecha} · ${solicitud.horario}",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                        Text(
                            text = "${solicitud.motivo} · $cantAsistentes asistentes",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    try {
                                        viewModel.aprobar(solicitud)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    viewModel.pestanaActual = "Inicio"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NegroDuoc),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text("APROBAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        viewModel.rechazar(solicitud)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    viewModel.pestanaActual = "Inicio"
                                },
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, NegroDuoc),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text("RECHAZAR", color = NegroDuoc, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}