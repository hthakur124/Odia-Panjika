package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

enum class OdiaThemePalette(
    val id: String,
    val odiaName: String,
    val englishName: String,
    val previewPrimary: Color,
    val previewAccent: Color
) {
    SINDOOR_MAROON(
        id = "sindoor_maroon",
        odiaName = "ସିନ୍ଦୂର ଲାଲ୍",
        englishName = "Vermilion Maroon",
        previewPrimary = Color(0xFF9E1B32),
        previewAccent = Color(0xFFF59E0B)
    ),
    NEELACHAKRA_BLUE(
        id = "neelachakra_blue",
        odiaName = "ନୀଳଚକ୍ର ନୀଳ",
        englishName = "Royal Temple Blue",
        previewPrimary = Color(0xFF1D4E89),
        previewAccent = Color(0xFFF59E0B)
    ),
    TEMPLE_TEAL(
        id = "temple_teal",
        odiaName = "ଶ୍ରୀକ୍ଷେତ୍ର ସବୁଜ",
        englishName = "Sacred Teal",
        previewPrimary = Color(0xFF0F766E),
        previewAccent = Color(0xFFF59E0B)
    ),
    GERUA_SAFFRON(
        id = "gerua_saffron",
        odiaName = "ଗେରୁଆ କେଶର",
        englishName = "Sacred Saffron",
        previewPrimary = Color(0xFFB45309),
        previewAccent = Color(0xFF0F766E)
    );

    companion object {
        fun fromId(id: String?): OdiaThemePalette =
            entries.firstOrNull { it.id == id } ?: SINDOOR_MAROON
    }
}

enum class OdiaTextSizeOption(
    val id: String,
    val scale: Float
) {
    NORMAL("normal", 1.0f),
    LARGE("large", 1.15f),
    EXTRA_LARGE("extra_large", 1.30f);

    companion object {
        fun fromId(id: String?): OdiaTextSizeOption =
            entries.firstOrNull { it.id == id } ?: LARGE
    }
}

@Immutable
data class OdiaAppColors(
    val headerStart: Color,
    val headerEnd: Color,
    val primaryAccent: Color,
    val primarySoft: Color,
    val secondaryAccent: Color,
    val secondarySoft: Color,
    val holidayRed: Color,
    val holidaySoft: Color,
    val festivalSaffron: Color,
    val creamBackground: Color,
    val cardSurface: Color,
    val outline: Color
)

private fun paletteColors(palette: OdiaThemePalette): OdiaAppColors = when (palette) {
    OdiaThemePalette.SINDOOR_MAROON -> OdiaAppColors(
        headerStart = VermilionDark,
        headerEnd = VermilionRed,
        primaryAccent = VermilionRed,
        primarySoft = VermilionSoft,
        secondaryAccent = TempleTeal,
        secondarySoft = TempleTealSoft,
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        festivalSaffron = SaffronGold,
        creamBackground = CreamBackground,
        cardSurface = CreamSurface,
        outline = CreamOutline
    )

    OdiaThemePalette.NEELACHAKRA_BLUE -> OdiaAppColors(
        headerStart = Color(0xFF133663),
        headerEnd = Color(0xFF1D4E89),
        primaryAccent = Color(0xFF1D4E89),
        primarySoft = Color(0xFFE6F0FA),
        secondaryAccent = TempleTeal,
        secondarySoft = TempleTealSoft,
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        festivalSaffron = SaffronGold,
        creamBackground = Color(0xFFF8FAFE),
        cardSurface = CreamSurface,
        outline = Color(0xFFD5E1F0)
    )

    OdiaThemePalette.TEMPLE_TEAL -> OdiaAppColors(
        headerStart = Color(0xFF0B534E),
        headerEnd = TempleTeal,
        primaryAccent = TempleTeal,
        primarySoft = TempleTealSoft,
        secondaryAccent = VermilionRed,
        secondarySoft = VermilionSoft,
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        festivalSaffron = SaffronGold,
        creamBackground = Color(0xFFF4FBF9),
        cardSurface = CreamSurface,
        outline = Color(0xFFCDE7E2)
    )

    OdiaThemePalette.GERUA_SAFFRON -> OdiaAppColors(
        headerStart = Color(0xFF92400E),
        headerEnd = Color(0xFFB45309),
        primaryAccent = Color(0xFFB45309),
        primarySoft = SaffronContainer,
        secondaryAccent = TempleTeal,
        secondarySoft = TempleTealSoft,
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        festivalSaffron = SaffronGold,
        creamBackground = Color(0xFFFFFBF0),
        cardSurface = CreamSurface,
        outline = Color(0xFFEBD9B7)
    )
}

val LocalOdiaColors = staticCompositionLocalOf {
    paletteColors(OdiaThemePalette.SINDOOR_MAROON)
}

val LocalOdiaTextScale = staticCompositionLocalOf { 1.15f }

private fun TextStyle.scaled(factor: Float): TextStyle = copy(
    fontSize = fontSize * factor,
    lineHeight = lineHeight * factor
)

private fun scaledTypography(scale: Float): Typography = Typography(
    displayLarge = OdiaTypography.displayLarge.scaled(scale),
    headlineLarge = OdiaTypography.headlineLarge.scaled(scale),
    headlineMedium = OdiaTypography.headlineMedium.scaled(scale),
    headlineSmall = OdiaTypography.headlineSmall.scaled(scale),
    titleLarge = OdiaTypography.titleLarge.scaled(scale),
    titleMedium = OdiaTypography.titleMedium.scaled(scale),
    titleSmall = OdiaTypography.titleSmall.scaled(scale),
    bodyLarge = OdiaTypography.bodyLarge.scaled(scale),
    bodyMedium = OdiaTypography.bodyMedium.scaled(scale),
    bodySmall = OdiaTypography.bodySmall.scaled(scale),
    labelLarge = OdiaTypography.labelLarge.scaled(scale),
    labelMedium = OdiaTypography.labelMedium.scaled(scale),
    labelSmall = OdiaTypography.labelSmall.scaled(scale)
)

private val OdiaShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun OdiaCalendarTheme(
    palette: OdiaThemePalette = OdiaThemePalette.SINDOOR_MAROON,
    textSizeOption: OdiaTextSizeOption = OdiaTextSizeOption.LARGE,
    content: @Composable () -> Unit
) {
    val odiaColors = paletteColors(palette)
    val colorScheme = lightColorScheme(
        primary = odiaColors.primaryAccent,
        onPrimary = PureWhite,
        primaryContainer = odiaColors.primarySoft,
        onPrimaryContainer = odiaColors.headerStart,
        secondary = odiaColors.festivalSaffron,
        onSecondary = InkPrimary,
        secondaryContainer = SaffronContainer,
        onSecondaryContainer = SaffronDeep,
        tertiary = odiaColors.secondaryAccent,
        onTertiary = PureWhite,
        tertiaryContainer = TempleTealContainer,
        onTertiaryContainer = TempleTealDark,
        background = odiaColors.creamBackground,
        onBackground = InkPrimary,
        surface = odiaColors.cardSurface,
        onSurface = InkPrimary,
        surfaceVariant = CreamSurfaceVariant,
        onSurfaceVariant = InkSecondary,
        outline = odiaColors.outline,
        outlineVariant = odiaColors.outline
    )

    CompositionLocalProvider(
        LocalOdiaColors provides odiaColors,
        LocalOdiaTextScale provides textSizeOption.scale
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = scaledTypography(textSizeOption.scale),
            shapes = OdiaShapes,
            content = content
        )
    }
}
