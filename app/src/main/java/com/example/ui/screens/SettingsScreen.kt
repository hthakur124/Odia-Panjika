package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
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
    isPrivacyOptionsRequired: Boolean = false,
    onOpenAdPrivacySettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    val clipboardManager = LocalClipboardManager.current
    val supportEmail = stringResource(R.string.settings_contact_email)
    var showEmailCopied by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

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

                    // Privacy Policy & Ad Preferences Card (Google Play / AdMob compliant)
                    PrivacyPolicyCard(
                        isPrivacyOptionsRequired = isPrivacyOptionsRequired,
                        onOpenPrivacyPolicy = { showPrivacyDialog = true },
                        onOpenAdPrivacySettings = onOpenAdPrivacySettings
                    )

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

    if (showPrivacyDialog) {
        PrivacyPolicyDialog(
            supportEmail = supportEmail,
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
private fun PrivacyPolicyCard(
    isPrivacyOptionsRequired: Boolean,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenAdPrivacySettings: () -> Unit
) {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.2.dp, colors.outline),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_privacy_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PrivacyTip,
                        contentDescription = null,
                        tint = colors.pureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.settings_privacy_title),
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.inkPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.settings_privacy_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkSecondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenPrivacyPolicy,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, colors.primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("open_privacy_policy_button")
                ) {
                    Text(
                        text = stringResource(R.string.btn_view_privacy_policy),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isPrivacyOptionsRequired) {
                    Button(
                        onClick = onOpenAdPrivacySettings,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.secondary,
                            contentColor = colors.pureWhite
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("open_ad_privacy_options_button")
                    ) {
                        Text(
                            text = stringResource(R.string.btn_ad_privacy_options),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrivacyPolicyDialog(
    supportEmail: String,
    onDismiss: () -> Unit
) {
    val colors = LocalOdiaColors.current
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        shape = RoundedCornerShape(22.dp),
        title = {
            Text(
                text = stringResource(R.string.settings_privacy_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Odia Panjika respects your privacy. All calendar reminders, text size choices, and theme color preferences are stored locally on your device and are never uploaded to our servers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkPrimary
                )
                Text(
                    text = "Advertising (Google AdMob)",
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "This app uses Google AdMob to display banner advertisements. Google and its partners may collect and process your device Advertising ID, approximate location, app interaction events, and diagnostic logs to deliver and measure ads in accordance with Google's Privacy Policy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkSecondary
                )
                Text(
                    text = "Your Choices & Opt-Out",
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "You can opt out of personalized ads anytime in Android Settings → Google → Ads, or update your consent preferences using Ad Privacy Settings where applicable.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.inkSecondary
                )
                Text(
                    text = "Contact: $supportEmail",
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.secondary,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = {
                        runCatching {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://policies.google.com/technologies/ads")
                            )
                            context.startActivity(intent)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, colors.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_google_ads_policy),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_close),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OdiaThemePalette.entries.forEach { palette ->
                    val isSelected = selectedPalette == palette
                    Surface(
                        onClick = { onSelectPalette(palette) },
                        shape = CircleShape,
                        color = if (isSelected) palette.previewPrimary.copy(alpha = 0.16f) else colors.surface,
                        border = BorderStroke(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) palette.previewPrimary else colors.outline
                        ),
                        modifier = Modifier
                            .size(50.dp)
                            .testTag("theme_palette_${palette.name.lowercase()}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(palette.previewPrimary)
                                    .border(2.dp, palette.previewAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = palette.titleEnglish,
                                        tint = colors.pureWhite,
                                        modifier = Modifier.size(20.dp)
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
