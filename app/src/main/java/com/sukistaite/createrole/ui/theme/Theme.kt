package com.sukistaite.createrole.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sukistaite.createrole.data.Config

/** 把主题名字映射成 Material3 色板（支持深浅色） */
fun themeScheme(themeName: String, dark: Boolean): androidx.compose.material3.ColorScheme {
    val t = Config.THEMES[themeName] ?: Config.THEMES.getValue(Config.DEFAULT_THEME)
    val primary = Color(android.graphics.Color.parseColor(t.primary))
    val onPrimary = if (dark) Color(0xFF3F0016) else Color.White
    val bg = if (dark) Color(0xFF1E1418) else Color(android.graphics.Color.parseColor(t.bg))
    val surface = if (dark) Color(0xFF2A1C22) else Color.White
    val onBg = if (dark) Color(0xFFF3DDE2) else Color(0xFF241419)
    val surfaceVariant = if (dark) Color(0xFF3A262D) else primary.copy(alpha = 0.10f)
    val onSurfaceVariant = if (dark) Color(0xFFD8BAC2) else Color(0xFF6E5158)
    val outline = if (dark) Color(0xFF6E5158) else primary.copy(alpha = 0.35f)
    val container = primary.copy(alpha = if (dark) 0.30f else 0.16f)
    val onContainer = if (dark) Color(0xFFFFD9E1) else Color(0xFF3F0016)
    return if (dark) darkColorScheme(
        primary = primary, onPrimary = onPrimary, primaryContainer = container,
        onPrimaryContainer = onContainer, secondary = primary, onSecondary = onPrimary,
        background = bg, onBackground = onBg, surface = surface, onSurface = onBg,
        surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant, outline = outline,
    ) else lightColorScheme(
        primary = primary, onPrimary = onPrimary, primaryContainer = container,
        onPrimaryContainer = onContainer, secondary = primary, onSecondary = onPrimary,
        background = bg, onBackground = onBg, surface = surface, onSurface = onBg,
        surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant, outline = outline,
    )
}

@Composable
fun CreateroleTheme(
    themeName: String = Config.DEFAULT_THEME,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = themeScheme(themeName, darkTheme),
        content = content
    )
}
