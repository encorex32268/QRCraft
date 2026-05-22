package com.lihan.qrcraft.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat




private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFEBFF69),
    surface = Color(0xFFEDF2F5),
    onSurface = Color(0xFF273037),
    outline = Color(0xFFCCD5DC),
    error = Color(0xFFF12244)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE2F652),
    surface = Color(0xFF12181C),
    onSurface = Color(0xFFE3E8EC),
    outline = Color(0xFF38454F),
    error = Color(0xFFFF6B82)
)

@Immutable
data class AppColors(
    val surfaceHigher: Color,
    val onSurfaceAlt: Color,
    val onSurfaceDisabled: Color,
    val overlay: Color,
    val onOverlay: Color,
    val link: Color,
    val linkBG: Color,
    val success: Color,
    val text: Color,
    val textBG: Color,
    val contact: Color,
    val contactBG: Color,
    val geo: Color,
    val geoBG: Color,
    val phone: Color,
    val phoneBG: Color,
    val wifi: Color,
    val wifiBG: Color
)

val LightColors = AppColors(
    surfaceHigher = Color(0xFFFFFFFF),
    onSurfaceAlt = Color(0xFF505F6A),
    onSurfaceDisabled = Color(0xFF8C99A2),
    overlay = Color(0x80000000),
    onOverlay = Color(0xFFFFFFFF),
    link = Color(0xFF373F05),
    linkBG = Color(0x4DEBFF69),
    success = Color(0xFF4DDA9D),
    text = Color(0xFF583DC5),
    textBG = Color(0x1A583DC5),
    contact = Color(0xFF259570),
    contactBG = Color(0x1A259570),
    geo = Color(0xFFB51D5C),
    geoBG = Color(0x1AB51D5C),
    phone = Color(0xFFC86017),
    phoneBG = Color(0x1AC86017),
    wifi = Color(0xFF1F44CD),
    wifiBG = Color(0x1A1F44CD)
)

val DarkColors = AppColors(
    surfaceHigher = Color(0xFF1E272E),
    onSurfaceAlt = Color(0xFF9AA7B1),
    onSurfaceDisabled = Color(0xFF5E6D7A),
    overlay = Color(0x80000000),
    onOverlay = Color(0xFFFFFFFF),
    link = Color(0xFFD3E25B),
    linkBG = Color(0x26EBFF69),
    success = Color(0xFF66EBB3),
    text = Color(0xFFA189FF),
    textBG = Color(0x26A189FF),
    contact = Color(0xFF4AD2A3),
    contactBG = Color(0x264AD2A3),
    geo = Color(0xFFFF629F),
    geoBG = Color(0x26FF629F),
    phone = Color(0xFFFF9D5C),
    phoneBG = Color(0x26FF9D5C),
    wifi = Color(0xFF7593FF),
    wifiBG = Color(0x267593FF)
)

val LocalAppColors = staticCompositionLocalOf { LightColors }

val ColorScheme.appColors
    @Composable
    get() = LocalAppColors.current


@Composable
fun QRCraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val targetColors = if (darkTheme) DarkColors else LightColors
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    SetIsStatusBarsContentLightColor(isLight = isSystemInDarkTheme())
    CompositionLocalProvider(
        LocalAppColors provides targetColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


@Composable
private fun SetIsStatusBarsContentLightColor(isLight: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !isLight
        }
    }
}