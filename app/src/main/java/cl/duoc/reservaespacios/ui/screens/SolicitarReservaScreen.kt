package cl.duoc.reservaespacios.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.reservaespacios.data.AppState

@Composable
fun SolicitarReservaScreen(navController: NavController, appState: AppState) {

    var fecha by remember { mutableStateOf("") }
    var bloqueHorario by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(androidx.compose.ui.Alignment.CenterStart)
            ) { Text("<-") }

            Text(
                text = "Solicitar Reserva",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bloqueHorario,
            onValueChange = { bloqueHorario = it },
            label = { Text("Bloque Horario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = capacidad,
            onValueChange = { capacidad = it },
            label = { Text("Capacidad") },
            modifier = Modifier.fillMaxWidth()
        )

        //Boton para reservar
        Button(
            onClick = {
                appState.agregarReserva(fecha, bloqueHorario, descripcion, capacidad)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar")
        }
    }
}

/* para visualizar solo la pantalla SolicitarReservaScreen en emulador y poder ver que funcione el DataStore
esto tiene que ejecutar MainActivity:

class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var appState: AppState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(applicationContext)
        appState = AppState(dataStoreManager)

        enableEdgeToEdge()
        setContent {
            ReservaEspaciosTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    SolicitarReservaScreen(
                        navController = navController,
                        appState = appState
                    )
                }
            }
        }
    }
}


*/