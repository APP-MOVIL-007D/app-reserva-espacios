package cl.duoc.reservaespacios.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.reservaespacios.R
import cl.duoc.reservaespacios.ui.componentes.CampoPassword
import cl.duoc.reservaespacios.ui.viewmodel.LoginViewModel
import cl.duoc.reservaespacios.ui.viewmodel.ReservasViewModel

@Composable
fun LoginScreen(
    reservasViewModel: ReservasViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {

    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var mostrarRecuperar by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //hay q cambiar esto pq el logo lo agarra del modo oscuro del sistema, falta opción
        //menu en la pantalla menú donde se pueda cambiar.
        val logo =
            (if (isSystemInDarkTheme()) R.drawable.logo_sede_tema_oscuro else R.drawable.logo_sede_tema_claro)

        Image(
            painter = painterResource(logo),
            contentDescription = "Logo Duoc Uc Sede Viña del Mar",
            modifier = Modifier.width(240.dp)
        )


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Reserva de espacios", style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Sede Viña del Mar", color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.correo,
            onValueChange = loginViewModel::onCorreoChange,
            label = { Text("Correo Duoc") },
            isError = uiState.errorCorreo != null,
            supportingText = { uiState.errorCorreo?.let { Text(it) } },
            trailingIcon = {
                if (uiState.errorCorreo != null) Icon(
                    Icons.Filled.Error, contentDescription = "Error"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoPassword(
            value = uiState.password,
            onValueChange = loginViewModel::onPasswordChange,
            errorTexto = uiState.errorPassword,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                loginViewModel.ingresar { correoLimpio ->
                    reservasViewModel.iniciarSesion(correoLimpio)
                    Toast.makeText(context, "Bienvenido/a ", Toast.LENGTH_SHORT).show()
                    navController.navigate("principal") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("INGRESAR")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { mostrarRecuperar = true }) {
            Text("¿Olvidaste tu contraseña?", textDecoration = TextDecoration.Underline)
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Acceso con tu cuenta institucional Duoc UC",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (mostrarRecuperar) {
        AlertDialog(
            onDismissRequest = { mostrarRecuperar = false },
            icon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            title = { Text("¿Olvidaste tu contraseña?", textAlign = TextAlign.Center) },
            text = {
                Text(
                    "Tu contraseña es la misma de tu cuenta Duoc UC. " +
                            "Para recuperarla, tienes que contactar con servicios digitales.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarRecuperar = false }) {
                    Text("Entendido")
                }
            }

        )
    }
}