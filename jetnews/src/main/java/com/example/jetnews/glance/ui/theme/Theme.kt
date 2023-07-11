package com.example.jetnews.glance.ui.theme

import androidx.glance.color.ColorProvider
import androidx.glance.material3.ColorProviders
import com.example.jetnews.ui.theme.DarkColors
import com.example.jetnews.ui.theme.LightColors

object JetnewsGlanceColorScheme {
    val colors = ColorProviders(
        light = LightColors,
        dark = DarkColors
    )

    val outlineVariant = ColorProvider(
        day = LightColors.onSurface.copy(alpha = 0.1f),
        night = DarkColors.onSurface.copy(alpha = 0.1f)
    )
}