package cl.duoc.reservaespacios.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.reservaespacios.R
import cl.duoc.reservaespacios.ui.theme.ReservaEspaciosTheme

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val logo = (if (isSystemInDarkTheme()) R.drawable.logo_sede_tema_oscuro else R.drawable.logo_sede_tema_claro)

        Image(
            painter = painterResource(logo),
            contentDescription = "Logo Duoc Uc Sede Viña del Mar",
            modifier = Modifier.width(240.dp)
        )


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Reserva de espacios",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Sede Viña del Mar")

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it},
            label = { Text("Correo Duoc") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("principal"){
                popUpTo("login") { inclusive = true}
            } },
            modifier = Modifier.fillMaxWidth(),
            enabled = correo.isNotBlank() && password.isNotBlank()
        ) {
            Text("Ingresar")
        }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    ReservaEspaciosTheme {
        LoginScreen(navController = rememberNavController())
    }
}