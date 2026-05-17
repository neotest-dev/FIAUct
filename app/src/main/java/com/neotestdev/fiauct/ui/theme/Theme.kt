package com.neotestdev.fiauct.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores corporativo refinado
private val ForcedLightColorScheme = lightColorScheme(
    primary = UCTBlue,
    onPrimary = Color.White,
    
    // Contenedores azules (como los círculos de los iconos y la TopBar)
    primaryContainer = UCTBlue,
    onPrimaryContainer = Color.White, // Esto hará que los iconos dentro de los círculos sean BLANCOS
    
    secondary = UCTBlue,
    onSecondary = Color.White,
    secondaryContainer = UCTBlue,
    onSecondaryContainer = Color.White,
    
    background = Color.White,
    surface = Color.White,
    onBackground = UCTBlue,
    onSurface = UCTBlue,
    
    surfaceVariant = Color(0xFFF5F5F5), // Un gris casi blanco para sutiles diferencias
    onSurfaceVariant = UCTBlue,
    outline = UCTBlue
)

@Composable
fun FIAUctTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = ForcedLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = UCTBlue.toArgb()
            window.navigationBarColor = Color.White.toArgb()
            
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = false
            windowInsetsController.isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}