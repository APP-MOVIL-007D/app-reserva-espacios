package cl.duoc.reservaespacios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duoc.reservaespacios.ui.screens.LoginScreen
import cl.duoc.reservaespacios.ui.screens.PrincipalScreen
import cl.duoc.reservaespacios.viewmodel.ReservasViewModel


@Composable
fun AppNavigation(navController: NavHostController, reservasViewModel: ReservasViewModel){
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("principal") {
            PrincipalScreen(reservasViewModel = reservasViewModel)
        }
    }

}