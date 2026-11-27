package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val OdiaLightColorScheme = lightColorScheme(
    primary = VermilionRed,
    onPrimary = PureWhite,
    primaryContainer = VermilionSoft,
    onPrimaryContainer = VermilionDark,
    secondary = SaffronGold,
    onSecondary = InkPrimary,
    secondaryContainer = SaffronContainer,
    onSecondaryContainer = SaffronDeep,
    tertiary = TempleTeal,
    onTertiary = PureWhite,
    tertiaryContainer = TempleTealContainer,
    onTertiaryContainer = TempleTealDark,
    background = CreamBackground,
    onBackground = InkPrimary,
    surface = CreamSurface,
    onSurface = InkPrimary,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = InkSecondary,
    outline = CreamOutline,
    outlineVariant = CreamOutline
)

private val OdiaDarkColorScheme = darkColorScheme(
    primary = DarkVermilion,
    onPrimary = DarkBackground,
    primaryContainer = VermilionDark,
    onPrimaryContainer = VermilionSoft,
    secondary = DarkSaffron,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSaffronContainer,
    onSecondaryContainer = SaffronContainer,
    tertiary = DarkTeal,
    onTertiary = DarkBackground,
    tertiaryContainer = TempleTealDark,
    onTertiaryContainer = TempleTealContainer,
    background = DarkBackground,
    onBackground = CreamBackground,
    surface = DarkSurface,
    onSurface = CreamBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutline
)

private val OdiaShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun OdiaCalendarTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OdiaDarkColorScheme else OdiaLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = OdiaTypography,
        shapes = OdiaShapes,
        content = content
    )
}
