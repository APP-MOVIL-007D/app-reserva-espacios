package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.reservaespacios.ui.componentes.BarraAdmin
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel2
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

//data class de pestaña
data class Pestana(
    val nombre: String,
    val iconoFull: ImageVector,
    val iconoVacio: ImageVector
)

//Para no tener q escribirlo 5 vececs DRY
val pestanas = listOf(
    Pestana("Inicio", Icons.Filled.Home, Icons.Outlined.Home),
    Pestana("Calendario", Icons.Filled.DateRange, Icons.Outlined.DateRange),
    Pestana("Espacios", Icons.Filled.Place, Icons.Outlined.Place),
    Pestana("Notificaciones", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    Pestana("Menu", Icons.Filled.Menu, Icons.Outlined.Menu),
)

@Composable
fun PrincipalScreen(reservasViewModel: ReservasViewModel, navController: NavController) {

    Scaffold(
        topBar = {
            if (reservasViewModel.esAdmin) {
                BarraAdmin()
            }
        },
        bottomBar = {
            NavigationBar {
                pestanas.forEach { pestana ->
                    NavigationBarItem(
                        selected = reservasViewModel.pestanaActual == pestana.nombre,
                        onClick = { reservasViewModel.pestanaActual = pestana.nombre },
                        icon = {
                            Icon(
                                imageVector = if (reservasViewModel.pestanaActual == pestana.nombre) pestana.iconoFull else pestana.iconoVacio,
                                contentDescription = pestana.nombre
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (reservasViewModel.pestanaActual) {
            "Inicio" -> if (reservasViewModel.esAdmin) {
                AdminSolicitudesScreen(reservasViewModel, modifier)
            } else {
                InicioScreen(
                    reservasViewModel,
                    Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    onVerEspacio = { nombre -> navController.navigate("calendarioEspacio/$nombre") }
                )
            }

            "Calendario" -> MisReservasScreen(reservasViewModel, modifier)
            "Notificaciones" -> NotificacionScreen(navController, modifier)
            "Menu" -> Box(modifier) {
                val menuViewModel2: MenuViewModel2 = viewModel()
                MenuScreen2(
                    menuViewModel = menuViewModel2,
                    reservasViewModel = reservasViewModel,
                    onCerrarSesion = {
                        reservasViewModel.cerrarSesion()
                        navController.navigate("login") {
                            popUpTo("principal") { inclusive = true }
                        }
                    },
                    onIrASolicitudes = { reservasViewModel.pestanaActual = "Inicio" },
                    onIrACargarHorario = { navController.navigate("cargarHorario") },
                    onIrANuevoEspacio = { navController.navigate("nuevoEspacio") }
                )
            }

            "Espacios" -> EspacioScreen(navController, reservasViewModel, modifier)

        }


    }
}
