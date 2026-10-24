package cl.duoc.reservaespacios.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import cl.duoc.reservaespacios.utils.MIN_PASSWORD

@Composable
fun CampoPassword(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorTexto: String? = null
) {
    val esValida = value.length >= MIN_PASSWORD
    var visible by remember { mutableStateOf(false) }
    val verde = if (isSystemInDarkTheme()) Color(0xFF66BB6A) else Color(0xFF2E7D32)
    val rojo = MaterialTheme.colorScheme.error

    val colorActual by animateColorAsState(
        targetValue = when {
            errorTexto != null -> rojo
            value.isEmpty() -> MaterialTheme.colorScheme.outline
            esValida -> verde
            else -> rojo
        },
        animationSpec = tween(durationMillis = 300),
        label = "colorPassword"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Contraseña") },
        singleLine = true,
        visualTransformation =
            if(visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña"
                )

            }
        },
        supportingText = {
            when {
                errorTexto != null -> Text(errorTexto)
                value.isNotEmpty() && !esValida -> Text("Faltan ${MIN_PASSWORD - value.length} caracteres")
            }
        },
        colors = OutlinedTextFieldDefaults
            .colors(
                focusedBorderColor = colorActual,
                unfocusedBorderColor = colorActual,
                unfocusedLabelColor = colorActual,
                focusedLabelColor = colorActual,
                cursorColor = colorActual,
                focusedSupportingTextColor = colorActual,
                unfocusedSupportingTextColor = colorActual
            ),
        modifier = modifier
    )
}