package cl.duoc.reservaespacios.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CuadroFecha(fecha: String, modifier : Modifier = Modifier){
    val meses =
        listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")

    val dia = fecha.substringBefore("/")
    val numeroMes = fecha.split("/").getOrNull(1)?.toIntOrNull() ?: 1
    val mes = meses.getOrElse(numeroMes - 1) { " "}

    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(dia, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary)
            Text(mes, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary)
        }

    }
}