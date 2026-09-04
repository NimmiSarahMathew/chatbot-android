package com.example.chatbot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Scheme = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    background = Bg,
    onBackground = Navy,
    surface = Card,
    onSurface = Navy,
)

@Composable
fun ChatbotTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
