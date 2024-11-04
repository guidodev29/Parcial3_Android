package com.api.parcial3_video.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define los colores
val GreenPrimary = Color(0xFF4CAF50)      // Verde Bosque
val GreenDark = Color(0xFF388E3C)         // Verde Oscuro
val BrownPrimary = Color(0xFF8D6E63)      // Marrón Tierra
val BeigeBackground = Color(0xFFF8F5E1)   // Fondo Beige Claro
val TextWhite = Color.White               // Blanco para textos

// Configura el esquema de colores
val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = TextWhite,
    background = BeigeBackground,
    onBackground = GreenDark,
    surface = Color.White,
    onSurface = GreenDark,
    primaryContainer = BrownPrimary,
)

// Función de tema de la aplicación
@Composable
fun EcotourismTheme(content: @Composable () -> Unit) {
    androidx.compose.material3.MaterialTheme(
        colorScheme = LightColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
