package cl.duoc.reservaespacios.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.reservaespacios.ui.theme.AmarilloDuoc
import cl.duoc.reservaespacios.ui.theme.NegroDuoc

//franja negra de arriba como en el mockup (pantallas 07 a 09), solo para el administrador
@Composable
fun BarraAdmin() {
    Text(
        text = "VISTA ADMINISTRADOR",
        color = AmarilloDuoc,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        textAlign = TextAlign.Center,
        //vdd q el orden del modifier si importa
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(NegroDuoc)
            .padding(vertical = 4.dp)
    )
}
