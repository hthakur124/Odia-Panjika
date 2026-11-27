package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.OdiaTextSizeOption
import com.example.ui.theme.OdiaThemePalette
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.TempleTealSoft

@Composable
fun SettingsScreen(
    uiState: OdiaCalendarUiState,
    onSelectTextSize: (OdiaTextSizeOption) -> Unit,
    onSelectThemePalette: (OdiaThemePalette) -> Unit,
    onOpenAboutCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val contactEmail = stringResource(R.string.settings_contact_email)
    val copiedMsg = stringResource(R.string.email_copied_feedback)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.creamBackground),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
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
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = SaffronHighlight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

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
                    // 1. Text Size Settings Card (ଅକ୍ଷର ଆକାର)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = colors.cardSurface,
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
                                        .background(colors.primaryAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.FormatSize,
                                        contentDescription = null,
                                        tint = PureWhite,
                                        modifier = Modifier.size(23.dp)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.settings_text_size_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = InkPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val options = listOf(
                                    Triple(OdiaTextSizeOption.NORMAL, R.string.settings_text_normal, 14.sp),
                                    Triple(OdiaTextSizeOption.LARGE, R.string.settings_text_large, 17.sp),
                                    Triple(OdiaTextSizeOption.EXTRA_LARGE, R.string.settings_text_extra_large, 20.sp)
                                )
                                options.forEach { (option, labelRes, sampleSize) ->
                                    val selected = uiState.textSizeOption == option
                                    Surface(
                                        onClick = { onSelectTextSize(option) },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (selected) colors.primaryAccent else colors.primarySoft,
                                        border = BorderStroke(
                                            width = if (selected) 2.dp else 1.dp,
                                            color = if (selected) colors.primaryAccent else colors.outline
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(68.dp)
                                            .testTag("text_size_${option.id}")
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(6.dp)
                                        ) {
                                            Text(
                                                text = "ଅ",
                                                fontSize = sampleSize,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) PureWhite else colors.primaryAccent
                                            )
                                            Text(
                                                text = stringResource(labelRes),
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) PureWhite else InkPrimary,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            // Live Odia Preview Strip
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = colors.creamBackground,
                                border = BorderStroke(1.dp, colors.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "ଜାନୁଆରୀ ୨୦୨୭ • ଉତ୍ତରାୟଣ ମକର ସଂକ୍ରାନ୍ତି",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = InkPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }

                    // 2. Theme Color Change Card (ରଙ୍ଗ ପରିବର୍ତ୍ତନ)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = colors.cardSurface,
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
                                        .background(SaffronGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Palette,
                                        contentDescription = null,
                                        tint = InkPrimary,
                                        modifier = Modifier.size(23.dp)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.settings_theme_color_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = InkPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OdiaThemePalette.entries.forEach { palette ->
                                    val selected = uiState.themePalette == palette
                                    Surface(
                                        onClick = { onSelectThemePalette(palette) },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (selected) colors.primarySoft else colors.creamBackground,
                                        border = BorderStroke(
                                            width = if (selected) 2.dp else 1.dp,
                                            color = if (selected) palette.previewPrimary else colors.outline
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("theme_palette_${palette.id}")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            // Dual Color Swatch
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(RoundedCornerShape(7.dp))
                                                        .background(palette.previewPrimary)
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(RoundedCornerShape(7.dp))
                                                        .background(palette.previewAccent)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = palette.odiaName,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = InkPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = palette.englishName,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = InkSecondary
                                                )
                                            }

                                            if (selected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .clip(CircleShape)
                                                        .background(palette.previewPrimary),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = null,
                                                        tint = PureWhite,
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

                    // 3. About Calendar (ଓଡ଼ିଆ ପଞ୍ଜିକା ବିଷୟରେ) right under Settings!
                    Surface(
                        onClick = onOpenAboutCalendar,
                        shape = RoundedCornerShape(18.dp),
                        color = SaffronContainer,
                        border = BorderStroke(1.5.dp, SaffronGold),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_open_about_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colors.primaryAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AccountBalance,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.settings_about_card_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = InkPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.settings_about_card_subtitle),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = InkSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // 4. Contact Card (odiaechoes@gmail.com)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = TempleTealSoft,
                        border = BorderStroke(1.3.dp, TempleTeal.copy(alpha = 0.5f)),
                        shadowElevation = 1.5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_contact_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(TempleTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = null,
                                        tint = PureWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.settings_contact_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TempleTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = contactEmail,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = InkPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:$contactEmail")
                                            putExtra(Intent.EXTRA_SUBJECT, "Odia Panjika Feedback")
                                        }
                                        try {
                                            context.startActivity(intent)
                                        } catch (_: Exception) {
                                            clipboardManager.setText(AnnotatedString(contactEmail))
                                            Toast.makeText(context, copiedMsg, Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TempleTeal,
                                        contentColor = PureWhite
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("contact_send_email_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.btn_send_email),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(contactEmail))
                                        Toast.makeText(context, copiedMsg, Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.2.dp, TempleTeal),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TempleTeal),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("contact_copy_email_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.ContentCopy,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.btn_copy_email),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
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
