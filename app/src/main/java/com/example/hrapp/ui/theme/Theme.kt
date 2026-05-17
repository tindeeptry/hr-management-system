package com.example.hrapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary        = Primary,
    onPrimary      = OnPrimary,
    secondary      = Secondary,
    background     = Background,
    surface        = Surface,
    error          = Error,
    onBackground   = OnBackground,
    onSurface      = OnSurface,
)

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}