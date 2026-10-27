package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.reservaespacios.ui.componentes.BarraAdmin
import cl.duoc.reservaespacios.ui.viewmodel.MenuViewModel
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
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                pestanas.forEach { pestana ->
                    val seleccionada = reservasViewModel.pestanaActual == pestana.nombre
                    NavigationBarItem(
                        selected = seleccionada,
                        onClick = { reservasViewModel.pestanaActual = pestana.nombre },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (seleccionada) pestana.iconoFull else pestana.iconoVacio,
                                    contentDescription = pestana.nombre
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(width = 16.dp, height = 3.dp)
                                        .background(
                                            if (seleccionada) MaterialTheme.colorScheme.secondary else Color.Transparent,
                                            RoundedCornerShape(50)
                                        )
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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

            "Calendario" -> MisReservasScreen(
                reservasViewModel,
                if (reservasViewModel.esAdmin) modifier.consumeWindowInsets(innerPadding)
                else Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            )
            "Notificaciones" -> NotificacionScreen(reservasViewModel, modifier)
            "Menu" -> Box(modifier) {
                val menuViewModel: MenuViewModel = viewModel()
                MenuScreen(
                    menuViewModel = menuViewModel,
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
