package cl.duoc.reservaespacios.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AmarilloDuoc,
    onPrimary = NegroDuoc,
    secondary = AmarilloDuoc,
    onSecondary = NegroDuoc,
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = NegroDuoc,
    onSurface = TextoOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = NegroDuoc,
    onPrimary = Color.White,
    secondary = AmarilloDuoc,
    onSecondary = NegroDuoc,
    background = FondoClaro,
    onBackground = NegroDuoc,
    surface = TarjetaClara,
    onSurface = NegroDuoc
)

@Composable
fun ReservaEspaciosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}