package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel

//data class de pestaña
data class Pestana(
    val nombre: String,
    val iconoFull: ImageVector,
    val iconoVacio: ImageVector
)

//Para no tener q escribirlo 5 vececsDRY
val pestanas = listOf(
    Pestana("Inicio", Icons.Filled.Home, Icons.Outlined.Home),
    Pestana("Calendario", Icons.Filled.DateRange, Icons.Outlined.DateRange),
    Pestana("Espacios", Icons.Filled.Place, Icons.Outlined.Place),
    Pestana("Notificaciones", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    Pestana("Menu", Icons.Filled.Menu, Icons.Outlined.Menu),
)

@Composable
fun PrincipalScreen(reservasViewModel: ReservasViewModel, navController : NavController) {

    var actualTab by remember { mutableStateOf("Inicio") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                pestanas.forEach { pestana ->
                    NavigationBarItem(
                        selected = actualTab == pestana.nombre,
                        onClick = { actualTab = pestana.nombre },
                        icon = {
                            Icon(
                                imageVector = if (actualTab == pestana.nombre) pestana.iconoFull else pestana.iconoVacio,
                                contentDescription = pestana.nombre
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (actualTab) {
            "Inicio" -> InicioScreen(reservasViewModel,modifier)
            "Calendario" -> MisReservasScreen(reservasViewModel, modifier)
            "Notificaciones" -> NotificacionScreen(navController, modifier)
            "Menu" -> MenuScreen(navController)
            "Espacios" -> EspacioScreen(navController, reservasViewModel, modifier)

        }


    }
}
