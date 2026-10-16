package cl.duoc.reservaespacios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import cl.duoc.reservaespacios.navigation.AppNavigation
import cl.duoc.reservaespacios.ui.theme.ReservaEspaciosTheme
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReservaEspaciosTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val reservasViewModel: ReservasViewModel = viewModel()
    Surface(modifier = Modifier.fillMaxSize()) {
        AppNavigation(navController, reservasViewModel)

    }
}
