package cl.duoc.reservaespacios.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.reservaespacios.ui.screens.LoginScreen
import cl.duoc.reservaespacios.ui.screens.PrincipalScreen
import cl.duoc.reservaespacios.ui.screens.SolicitarReservaScreen
import cl.duoc.reservaespacios.ui.screens.CalendarioEspacioScreen
import cl.duoc.reservaespacios.ui.screens.AdminNuevoEspacioScreen
import cl.duoc.reservaespacios.ui.screens.CargarHorarioScreen
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun AppNavigation(navController: NavHostController, reservasViewModel: ReservasViewModel) {
    //se calcula a cada rato la sesióin deneuevo , cada vez q se cambia el correo,
    val inicio =
        remember { if (reservasViewModel.correoUsuario.isNotEmpty()) "principal" else "login" }

    NavHost(navController = navController, startDestination = inicio) {
        composable("login") {
            LoginScreen(navController = navController, reservasViewModel = reservasViewModel)
        }
        composable("principal") {
            PrincipalScreen(navController = navController, reservasViewModel = reservasViewModel)
        }
        composable("solicitarReserva") {
            SolicitarReservaScreen(
                onVolver = { navController.popBackStack() },
                onGuardarReserva = { espacio, fecha, horario, motivo ->
                    reservasViewModel.agregarReserva(espacio, fecha, horario, motivo)
                    reservasViewModel.pestanaActual = "Calendario"
                    navController.popBackStack()
                }
            )
        }
        composable("solicitarReserva/{espacio}") { backStackEntry ->
            val espacioSeleccionado = backStackEntry.arguments?.getString("espacio") ?: ""
            SolicitarReservaScreen(
                espacioParam = espacioSeleccionado,
                onVolver = { navController.popBackStack() },
                onGuardarReserva = { espacio, fecha, horario, motivo ->
                    reservasViewModel.agregarReserva(espacio, fecha, horario, motivo)
                    reservasViewModel.pestanaActual = "Calendario"
                    navController.popBackStack()
                }
            )
        }
        composable("solicitarReserva/{espacio}/{fecha}/{hora}") { backStackEntry ->
            val espacioSeleccionado = backStackEntry.arguments?.getString("espacio") ?: ""
            val fechaSeleccionada = backStackEntry.arguments?.getString("fecha") ?: ""
            val horaSeleccionada = backStackEntry.arguments?.getString("hora") ?: ""
            SolicitarReservaScreen(
                espacioParam = espacioSeleccionado,
                fechaParam = fechaSeleccionada,
                horaInicioParam = horaSeleccionada,
                onVolver = { navController.popBackStack() },
                onGuardarReserva = { espacio, fecha, horario, motivo ->
                    reservasViewModel.agregarReserva(espacio, fecha, horario, motivo)
                    reservasViewModel.pestanaActual = "Calendario"
                    navController.popBackStack()
                }
            )
        }
        composable("calendarioEspacio/{espacio}") { backStackEntry ->
            val espacioSeleccionado = backStackEntry.arguments?.getString("espacio") ?: ""
            CalendarioEspacioScreen(
                navController = navController,
                reservasViewModel = reservasViewModel,
                espacioNombre = espacioSeleccionado
            )
        }

        composable("nuevoEspacio") {
            AdminNuevoEspacioScreen(
                reservasViewModel = reservasViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable("cargarHorario") {
            CargarHorarioScreen ( onVolver = { navController.popBackStack() } )
        }

    }

}