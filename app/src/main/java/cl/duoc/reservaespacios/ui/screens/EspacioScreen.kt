package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.reservaespacios.ui.theme.ReservaEspaciosTheme

data class Sala(val nombre: String, val capacidad: Int)

@Composable
fun EspacioScreen (navController: NavController, modifier: Modifier = Modifier){

    var buscarEspacio by remember { mutableStateOf("") }

    /* Acá se pueden agregar mas espacios de los que ya estan acá*/
    val listaEspacios = listOf(
        Sala("Sala 01", 30),
        Sala("Sala 02", 25),
        Sala("Sala 03", 40),
        Sala("Sala 04", 35),
        Sala("Sala 05", 20)
    )

    val espaciosFiltrados = listaEspacios.filter {
        it.nombre.contains(buscarEspacio, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp))
    {

        Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Espacios",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.Black,
    )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscarEspacio,
            onValueChange = { buscarEspacio = it },
            label = { Text("Buscar espacio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cantidad de espacios disponibles: ${listaEspacios.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(espaciosFiltrados) { espacio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = espacio.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Capacidad de espacio: ${espacio.capacidad}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { navController.navigate("solicitarReserva") },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Ver disponibilidad") }


                    }
                }
            }

        }

    }

}


//@Preview(showBackground = true)
//@Composable
//fun EspacioScreenPreview() {
//    ReservaEspaciosTheme {
//        EspacioScreen(navController = rememberNavController())
//
//    }}




