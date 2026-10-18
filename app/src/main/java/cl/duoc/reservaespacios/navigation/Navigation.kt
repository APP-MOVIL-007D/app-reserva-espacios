package cl.duoc.reservaespacios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.reservaespacios.ui.screens.LoginScreen
import cl.duoc.reservaespacios.ui.screens.PrincipalScreen
import cl.duoc.reservaespacios.ui.screens.SolicitarReservaScreen
import cl.duoc.reservaespacios.ui.screens.CalendarioEspacioScreen
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel


@Composable
fun AppNavigation(navController: NavHostController, reservasViewModel: ReservasViewModel){
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("principal") {
            PrincipalScreen(navController = navController, reservasViewModel = reservasViewModel)
        }
        composable("solicitarReserva"){
            SolicitarReservaScreen(navController = navController, reservasViewModel = reservasViewModel)

        }
        composable("solicitarReserva/{espacio}") { backStackEntry ->
            val espacioSeleccionado = backStackEntry.arguments?.getString("espacio") ?: ""
            SolicitarReservaScreen(
                navController = navController,
                reservasViewModel = reservasViewModel,
                espacioParam = espacioSeleccionado
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

    }

}