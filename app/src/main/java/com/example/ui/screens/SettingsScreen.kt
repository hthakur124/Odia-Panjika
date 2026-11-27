package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.OdiaCalendarUiState
import com.example.ui.components.PattachitraHeaderContainer
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.OdiaTextSizeOption
import com.example.ui.theme.OdiaThemePalette
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    uiState: OdiaCalendarUiState,
    onSelectTextSize: (OdiaTextSizeOption) -> Unit,
    onSelectThemePalette: (OdiaThemePalette) -> Unit,
    onOpenHolidaysList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    val clipboardManager = LocalClipboardManager.current
    val supportEmail = stringResource(R.string.settings_contact_email)
    var showEmailCopied by remember { mutableStateOf(false) }

    LaunchedEffect(showEmailCopied) {
        if (showEmailCopied) {
            delay(2500)
            showEmailCopied = false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // 1. Curved Heritage Header
        item {
            PattachitraHeaderContainer {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.pureWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.accentHighlight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2. Settings Controls + About App directly underneath
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Text Size Card
                    TextSizeSettingsCard(
                        selectedOption = uiState.textSizeOption,
                        onSelectOption = onSelectTextSize
                    )

                    // Theme Color Palette Card
                    ThemeColorSettingsCard(
                        selectedPalette = uiState.themePalette,
                        onSelectPalette = onSelectThemePalette
                    )

                    // Contact Email Card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = colors.surface,
                        border = BorderStroke(1.2.dp, colors.outline),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_contact_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(colors.secondary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = null,
                                        tint = colors.pureWhite,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.settings_contact_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = colors.inkPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = supportEmail,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.inkSecondary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(supportEmail))
                                        showEmailCopied = true
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(colors.secondarySoft)
                                        .testTag("copy_support_email_button")
                                ) {
                                    Icon(
                                        imageVector = if (showEmailCopied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                                        contentDescription = stringResource(R.string.cd_copy_email),
                                        tint = colors.secondary
                                    )
                                }
                            }

                            if (showEmailCopied) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = colors.secondaryContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.email_copied_feedback),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = colors.secondaryDark,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Under Settings: Full About Odia Panjika & Shri Jagannath Parampara section
                    AboutCalendarCardsSection(
                        uiState = uiState,
                        onOpenHolidaysList = onOpenHolidaysList
                    )
                }
            }
        }
    }
}

@Composable
private fun TextSizeSettingsCard(
    selectedOption: OdiaTextSizeOption,
    onSelectOption: (OdiaTextSizeOption) -> Unit
) {
    val colors = LocalOdiaColors.current

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.2.dp, colors.outline),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_text_size_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FormatSize,
                        contentDescription = null,
                        tint = colors.pureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.settings_text_size_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val options = listOf(
                    OdiaTextSizeOption.NORMAL to R.string.settings_text_normal,
                    OdiaTextSizeOption.LARGE to R.string.settings_text_large,
                    OdiaTextSizeOption.EXTRA_LARGE to R.string.settings_text_extra_large
                )
                options.forEach { (option, labelRes) ->
                    val isSelected = selectedOption == option
                    Surface(
                        onClick = { onSelectOption(option) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.primary else colors.surfaceVariant,
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) colors.primary else colors.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("text_size_${option.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "ଅ",
                                fontSize = when (option) {
                                    OdiaTextSizeOption.NORMAL -> 16.sp
                                    OdiaTextSizeOption.LARGE -> 20.sp
                                    OdiaTextSizeOption.EXTRA_LARGE -> 24.sp
                                },
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) colors.pureWhite else colors.primary
                            )
                            Text(
                                text = stringResource(labelRes),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) colors.pureWhite else colors.inkPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeColorSettingsCard(
    selectedPalette: OdiaThemePalette,
    onSelectPalette: (OdiaThemePalette) -> Unit
) {
    val colors = LocalOdiaColors.current

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.2.dp, colors.outline),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_theme_color_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.accent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Palette,
                        contentDescription = null,
                        tint = colors.inkPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.settings_theme_color_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OdiaThemePalette.entries.forEach { palette ->
                    val isSelected = selectedPalette == palette
                    Surface(
                        onClick = { onSelectPalette(palette) },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) colors.primarySoft else colors.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) palette.previewPrimary else colors.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("theme_palette_${palette.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // 3-dot palette preview
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(palette.previewPrimary)
                                            .border(1.dp, colors.pureWhite, CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(palette.previewAccent)
                                            .align(Alignment.CenterVertically)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(palette.previewSecondary)
                                            .align(Alignment.CenterVertically)
                                    )
                                }

                                Column {
                                    Text(
                                        text = palette.titleOdia,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = colors.inkPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = palette.titleEnglish,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colors.inkSecondary
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(palette.previewPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = colors.pureWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
