package cl.duoc.reservaespacios.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.reservaespacios.ui.componentes.BarraAdmin
import cl.duoc.reservaespacios.ui.viewmodel.ArchivoCargado
import cl.duoc.reservaespacios.ui.viewmodel.CargarHorarioViewModel

@Composable
fun CargarHorarioScreen(
    viewModel: CargarHorarioViewModel = viewModel (),
    modifier: Modifier = Modifier,
    onVolver: () -> Unit
) {
    val seleccionadorArchivos = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val nombreExtraido = it.lastPathSegment ?: "documento_desconocido"
            viewModel.archivoSeleccionadoDesdeDispositivo(it, nombreExtraido)
        }
    }

    // Estructura del CargarHorario
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BarraAdmin()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            // Encabezado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Cargar Horario",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Tarjeta de Subida de Archivo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Archivo CSV", style = MaterialTheme.typography.displaySmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Horario de clases del semestre",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Archivo CSV o Excel · máximo 10 MB",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            seleccionadorArchivos.launch("*/*")
                        }
                    ) {
                        Text("SUBIR ARCHIVO")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Última carga")

            Spacer(modifier = Modifier.height(12.dp))

            // dibuja si hay un archivo cargado
            viewModel.ultimaCarga?.let { archivo ->
                TarjetaArchivoCargado(archivo = archivo)
            }
        }
    }

    if (viewModel.mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelarCarga() },
            title = { Text("Confirmar paquete de datos") },
            text = { Text("Estás a punto de procesar el archivo: \n\n${viewModel.nombreArchivoPendiente}\n\n ¿Deseas continuar y actualizar los horarios en la base de datos?") },
            confirmButton = {
                TextButton (onClick = { viewModel.confirmarCarga() }) {
                    Text("Sí, cargar archivo")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelarCarga() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TarjetaArchivoCargado(
    archivo: ArchivoCargado,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📁", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = archivo.nombre,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = archivo.peso,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = archivo.fecha,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = archivo.estado,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
