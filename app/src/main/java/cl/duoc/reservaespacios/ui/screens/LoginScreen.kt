package cl.duoc.reservaespacios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reserva de espacios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
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

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "¿Olvidaste tu contraseña?")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = correo.isNotBlank() && password.isNotBlank()
        ) {
            Text("Ingresar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}