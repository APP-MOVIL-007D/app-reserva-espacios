package cl.duoc.reservaespacios.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.duoc.reservaespacios.ui.viewmodel.CargaUiState


//componente mas q nada para no repetir el mismo when en cada pantalla
@Composable
fun EstadoCarga(
    estado: CargaUiState,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    when (estado) {
        is CargaUiState.Cargando -> CircularProgressIndicator(modifier)
        is CargaUiState.SinConexion -> Column(modifier){
            Text("No hay conexión a internet", color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onReintentar) { Text("Reintentar")}
        }

        is CargaUiState.Error -> Text(
            estado.mensaje, color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
        is CargaUiState.Listo -> contenido()
    }
}