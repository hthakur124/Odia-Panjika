package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * User-selectable app-wide text size options.
 */
enum class OdiaTextSizeOption(
    val fontScaleMultiplier: Float,
    val labelOdia: String,
    val labelEnglish: String
) {
    NORMAL(1.0f, "ସାଧାରଣ", "Normal"),
    LARGE(1.14f, "ବଡ଼", "Large"),
    EXTRA_LARGE(1.28f, "ଅତି ବଡ଼", "Extra Large");

    companion object {
        fun fromKey(key: String?): OdiaTextSizeOption =
            entries.firstOrNull { it.name.equals(key, ignoreCase = true) } ?: NORMAL
    }
}

/**
 * Rich Odisha-inspired color palettes selectable in Settings.
 */
enum class OdiaThemePalette(
    val titleOdia: String,
    val titleEnglish: String,
    val previewPrimary: Color,
    val previewAccent: Color,
    val previewSecondary: Color
) {
    VERMILION_HERITAGE(
        titleOdia = "ସିନ୍ଦୂର ଲାଲ୍",
        titleEnglish = "Vermilion Red",
        previewPrimary = Color(0xFF9E1B32),
        previewAccent = Color(0xFFF59E0B),
        previewSecondary = Color(0xFF0F766E)
    ),
    TEMPLE_TEAL(
        titleOdia = "ନୀଳକଣ୍ଠ ସବୁଜ",
        titleEnglish = "Temple Teal",
        previewPrimary = Color(0xFF0F766E),
        previewAccent = Color(0xFFF59E0B),
        previewSecondary = Color(0xFF9E1B32)
    ),
    SAFFRON_SUNRISE(
        titleOdia = "ଗେରୁଆ ସୂର୍ଯ୍ୟ",
        titleEnglish = "Saffron Gold",
        previewPrimary = Color(0xFFC2410C),
        previewAccent = Color(0xFFF59E0B),
        previewSecondary = Color(0xFF0F766E)
    ),
    KONARK_INDIGO(
        titleOdia = "କୋଣାର୍କ ନୀଳ",
        titleEnglish = "Konark Indigo",
        previewPrimary = Color(0xFF312E81),
        previewAccent = Color(0xFFF59E0B),
        previewSecondary = Color(0xFF0F766E)
    ),
    SHREE_EMERALD(
        titleOdia = "ଶ୍ରୀକ୍ଷେତ୍ର ସବୁଜ",
        titleEnglish = "Shree Emerald",
        previewPrimary = Color(0xFF166534),
        previewAccent = Color(0xFFF59E0B),
        previewSecondary = Color(0xFF9E1B32)
    );

    companion object {
        fun fromKey(key: String?): OdiaThemePalette =
            entries.firstOrNull { it.name.equals(key, ignoreCase = true) } ?: VERMILION_HERITAGE
    }
}

@Immutable
data class OdiaColorTokens(
    val primary: Color,
    val primaryDark: Color,
    val primarySoft: Color,
    val accent: Color,
    val accentDeep: Color,
    val accentContainer: Color,
    val accentHighlight: Color,
    val secondary: Color,
    val secondaryDark: Color,
    val secondaryContainer: Color,
    val secondarySoft: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val holidayRed: Color,
    val holidaySoft: Color,
    val inkPrimary: Color,
    val inkSecondary: Color,
    val pureWhite: Color
)

private fun tokensForPalette(palette: OdiaThemePalette): OdiaColorTokens = when (palette) {
    OdiaThemePalette.VERMILION_HERITAGE -> OdiaColorTokens(
        primary = VermilionRed,
        primaryDark = VermilionDark,
        primarySoft = VermilionSoft,
        accent = SaffronGold,
        accentDeep = SaffronDeep,
        accentContainer = SaffronContainer,
        accentHighlight = SaffronHighlight,
        secondary = TempleTeal,
        secondaryDark = TempleTealDark,
        secondaryContainer = TempleTealContainer,
        secondarySoft = TempleTealSoft,
        background = CreamBackground,
        surface = CreamSurface,
        surfaceVariant = CreamSurfaceVariant,
        outline = CreamOutline,
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        inkPrimary = InkPrimary,
        inkSecondary = InkSecondary,
        pureWhite = PureWhite
    )

    OdiaThemePalette.TEMPLE_TEAL -> OdiaColorTokens(
        primary = Color(0xFF0F766E),
        primaryDark = Color(0xFF115E59),
        primarySoft = Color(0xFFCCFBF1),
        accent = SaffronGold,
        accentDeep = SaffronDeep,
        accentContainer = SaffronContainer,
        accentHighlight = SaffronHighlight,
        secondary = Color(0xFF9E1B32),
        secondaryDark = Color(0xFF7D1326),
        secondaryContainer = Color(0xFFFCE8EC),
        secondarySoft = Color(0xFFFDF2F4),
        background = Color(0xFFF4FAF8),
        surface = PureWhite,
        surfaceVariant = Color(0xFFE5F3F0),
        outline = Color(0xFFC7E0DB),
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        inkPrimary = InkPrimary,
        inkSecondary = InkSecondary,
        pureWhite = PureWhite
    )

    OdiaThemePalette.SAFFRON_SUNRISE -> OdiaColorTokens(
        primary = Color(0xFFC2410C),
        primaryDark = Color(0xFF9A3412),
        primarySoft = Color(0xFFFFEDD5),
        accent = SaffronGold,
        accentDeep = SaffronDeep,
        accentContainer = SaffronContainer,
        accentHighlight = SaffronHighlight,
        secondary = TempleTeal,
        secondaryDark = TempleTealDark,
        secondaryContainer = TempleTealContainer,
        secondarySoft = TempleTealSoft,
        background = Color(0xFFFFF9EE),
        surface = PureWhite,
        surfaceVariant = Color(0xFFFAEBD6),
        outline = Color(0xFFEBD2B0),
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        inkPrimary = InkPrimary,
        inkSecondary = InkSecondary,
        pureWhite = PureWhite
    )

    OdiaThemePalette.KONARK_INDIGO -> OdiaColorTokens(
        primary = Color(0xFF312E81),
        primaryDark = Color(0xFF1E1B4B),
        primarySoft = Color(0xFFE0E7FF),
        accent = SaffronGold,
        accentDeep = SaffronDeep,
        accentContainer = SaffronContainer,
        accentHighlight = SaffronHighlight,
        secondary = TempleTeal,
        secondaryDark = TempleTealDark,
        secondaryContainer = TempleTealContainer,
        secondarySoft = TempleTealSoft,
        background = Color(0xFFF7F7FC),
        surface = PureWhite,
        surfaceVariant = Color(0xFFECEBF8),
        outline = Color(0xFFD2D0EC),
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        inkPrimary = InkPrimary,
        inkSecondary = InkSecondary,
        pureWhite = PureWhite
    )

    OdiaThemePalette.SHREE_EMERALD -> OdiaColorTokens(
        primary = Color(0xFF166534),
        primaryDark = Color(0xFF14532D),
        primarySoft = Color(0xFFDCFCE7),
        accent = SaffronGold,
        accentDeep = SaffronDeep,
        accentContainer = SaffronContainer,
        accentHighlight = SaffronHighlight,
        secondary = Color(0xFF0F766E),
        secondaryDark = Color(0xFF115E59),
        secondaryContainer = TempleTealContainer,
        secondarySoft = TempleTealSoft,
        background = Color(0xFFF6FAF4),
        surface = PureWhite,
        surfaceVariant = Color(0xFFE8F2E4),
        outline = Color(0xFFCBE0C5),
        holidayRed = VermilionRed,
        holidaySoft = VermilionSoft,
        inkPrimary = InkPrimary,
        inkSecondary = InkSecondary,
        pureWhite = PureWhite
    )
}

val LocalOdiaColors = staticCompositionLocalOf {
    tokensForPalette(OdiaThemePalette.VERMILION_HERITAGE)
}

private val OdiaShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun OdiaCalendarTheme(
    themePalette: OdiaThemePalette = OdiaThemePalette.VERMILION_HERITAGE,
    textSizeOption: OdiaTextSizeOption = OdiaTextSizeOption.NORMAL,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val odiaColors = tokensForPalette(themePalette)
    val colorScheme = if (darkTheme) {
        darkColorScheme(
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
    } else {
        lightColorScheme(
            primary = odiaColors.primary,
            onPrimary = odiaColors.pureWhite,
            primaryContainer = odiaColors.primarySoft,
            onPrimaryContainer = odiaColors.primaryDark,
            secondary = odiaColors.accent,
            onSecondary = odiaColors.inkPrimary,
            secondaryContainer = odiaColors.accentContainer,
            onSecondaryContainer = odiaColors.accentDeep,
            tertiary = odiaColors.secondary,
            onTertiary = odiaColors.pureWhite,
            tertiaryContainer = odiaColors.secondaryContainer,
            onTertiaryContainer = odiaColors.secondaryDark,
            background = odiaColors.background,
            onBackground = odiaColors.inkPrimary,
            surface = odiaColors.surface,
            onSurface = odiaColors.inkPrimary,
            surfaceVariant = odiaColors.surfaceVariant,
            onSurfaceVariant = odiaColors.inkSecondary,
            outline = odiaColors.outline,
            outlineVariant = odiaColors.outline
        )
    }

    val currentDensity = LocalDensity.current
    val scaledDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * textSizeOption.fontScaleMultiplier
    )

    CompositionLocalProvider(
        LocalOdiaColors provides odiaColors,
        LocalDensity provides scaledDensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = OdiaTypography,
            shapes = OdiaShapes,
            content = content
        )
    }
}
