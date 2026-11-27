package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Brightness3
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalendarDate
import com.example.data.CalendarReminder
import com.example.data.OdiaDayEntry
import com.example.data.OdiaFormatting
import com.example.data.SolarTimes
import com.example.ui.theme.LocalOdiaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailBottomSheet(
    date: CalendarDate,
    entry: OdiaDayEntry?,
    solarTimes: SolarTimes,
    dayReminders: List<CalendarReminder>,
    onDismiss: () -> Unit,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onAddReminder: () -> Unit,
    onDeleteReminder: (String) -> Unit
) {
    val colors = LocalOdiaColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.background,
        contentColor = colors.inkPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("day_detail_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 18.dp)
                .padding(bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Bold Maroon Date Hero Banner
            DayHeroBanner(
                date = date,
                entry = entry,
                onPreviousDay = onPreviousDay,
                onNextDay = onNextDay,
                onDismiss = onDismiss
            )

            // 2. Festival Card (shown ONLY when a festival exists for this date)
            if (entry?.hasFestival == true) {
                FestivalSpotlightCard(festivalName = entry.festival)
            }

            // 3. Tithi, Nakshatra & Rashi — Large Bold Cards
            PanchangAttributesSection(entry = entry)

            // 4. Sunrise & Sunset Card
            SolarTimingsCard(solarTimes = solarTimes)

            // 5. Shubha Din Card — SHOWN ONLY IF AVAILABLE FOR THIS DATE!
            if (entry?.hasShubhaDin == true) {
                ShubhaDinCard(shubhaDin = entry.shubhaDin)
            }

            // 6. Reminders for this date + Add Reminder Button
            DayRemindersSection(
                reminders = dayReminders,
                onAddReminder = onAddReminder,
                onDeleteReminder = onDeleteReminder
            )
        }
    }
}

@Composable
private fun DayHeroBanner(
    date: CalendarDate,
    entry: OdiaDayEntry?,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalOdiaColors.current
    val isHoliday = entry?.let { it.isFullMaroonHoliday || it.isSunday } ?: false
    val odiaWeekday = OdiaFormatting.fullWeekdayOdia(date.dayOfWeek)
    val englishWeekday = entry?.weekdayEnglish?.ifBlank { null } ?: ""

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(colors.primaryDark, colors.primary)
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isHoliday) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = colors.accent
                            ) {
                                Text(
                                    text = stringResource(R.string.label_holiday_badge),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.inkPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        } else {
                            Text(
                                text = OdiaFormatting.sakabdaSamvatLine(date.year, date.month),
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.accentHighlight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("close_day_detail_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.cd_close_sheet),
                            tint = colors.pureWhite
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onPreviousDay,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(colors.pureWhite.copy(alpha = 0.16f))
                            .testTag("detail_prev_day_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.cd_previous_day),
                            tint = colors.pureWhite
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp)
                    ) {
                        Text(
                            text = OdiaFormatting.formatDateOdiaFull(date),
                            style = MaterialTheme.typography.headlineMedium,
                            color = colors.pureWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = buildString {
                                append(odiaWeekday)
                                if (englishWeekday.isNotBlank()) {
                                    append("  •  ")
                                    append(englishWeekday)
                                }
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = colors.accentHighlight,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = OdiaFormatting.formatDateEnglishFull(date),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.pureWhite.copy(alpha = 0.88f)
                        )
                    }

                    IconButton(
                        onClick = onNextDay,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(colors.pureWhite.copy(alpha = 0.16f))
                            .testTag("detail_next_day_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.cd_next_day),
                            tint = colors.pureWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FestivalSpotlightCard(festivalName: String) {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.accent,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detail_festival_card")
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
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Celebration,
                    contentDescription = null,
                    tint = colors.pureWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.label_festival),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.inkPrimary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = festivalName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 21.sp,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PanchangAttributesSection(entry: OdiaDayEntry?) {
    val colors = LocalOdiaColors.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PanchangInfoTile(
                label = stringResource(R.string.label_tithi),
                value = entry?.tithi?.ifBlank { "—" } ?: "—",
                icon = Icons.Rounded.Brightness3,
                accentColor = colors.pureWhite,
                badgeColor = colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("detail_tithi_tile")
            )
            PanchangInfoTile(
                label = stringResource(R.string.label_nakshatra),
                value = entry?.nakshatra?.ifBlank { "—" } ?: "—",
                icon = Icons.Rounded.Star,
                accentColor = colors.inkPrimary,
                badgeColor = colors.accent,
                modifier = Modifier
                    .weight(1f)
                    .testTag("detail_nakshatra_tile")
            )
        }

        PanchangInfoTile(
            label = stringResource(R.string.label_rashi),
            value = entry?.rashi?.ifBlank { "—" } ?: "—",
            icon = Icons.Rounded.Public,
            accentColor = colors.pureWhite,
            badgeColor = colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("detail_rashi_tile")
        )
    }
}

@Composable
private fun PanchangInfoTile(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = colors.surface,
        border = BorderStroke(1.2.dp, colors.outline),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.inkSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SolarTimingsCard(solarTimes: SolarTimes) {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.surface,
        border = BorderStroke(1.2.dp, colors.outline),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detail_solar_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.label_solar_location),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.secondary,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colors.secondaryContainer
                ) {
                    Text(
                        text = solarTimes.daylightOdia,
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.secondaryDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            SolarHorizonArc()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.accentContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WbSunny,
                            contentDescription = null,
                            tint = colors.accentDeep,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.label_sunrise),
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.inkSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = solarTimes.sunriseOdia,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            color = colors.inkPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.label_sunset),
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.inkSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = solarTimes.sunsetOdia,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            color = colors.inkPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.primarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WbTwilight,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShubhaDinCard(shubhaDin: String) {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.secondaryContainer,
        border = BorderStroke(1.5.dp, colors.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detail_shubha_din_card")
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
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(colors.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = null,
                    tint = colors.pureWhite,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.label_shubha_din),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.secondaryDark,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = shubhaDin,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 19.sp,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DayRemindersSection(
    reminders: List<CalendarReminder>,
    onAddReminder: () -> Unit,
    onDeleteReminder: (String) -> Unit
) {
    val colors = LocalOdiaColors.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (reminders.isNotEmpty()) {
            Text(
                text = stringResource(R.string.day_reminders_heading),
                style = MaterialTheme.typography.titleSmall,
                color = colors.secondaryDark,
                fontWeight = FontWeight.Bold
            )

            reminders.forEach { reminder ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colors.secondarySoft,
                    border = BorderStroke(1.dp, colors.secondary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = colors.secondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = reminder.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.inkPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = buildString {
                                    append(OdiaFormatting.formatTime12HourOdia(reminder.hour, reminder.minute))
                                    if (reminder.note.isNotBlank()) {
                                        append("  •  ")
                                        append(reminder.note)
                                    }
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.inkSecondary
                            )
                        }
                        IconButton(
                            onClick = { onDeleteReminder(reminder.id) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = stringResource(R.string.cd_delete_reminder),
                                tint = colors.holidayRed
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onAddReminder,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.secondary,
                contentColor = colors.pureWhite
            ),
            contentPadding = PaddingValues(vertical = 14.dp, horizontal = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("detail_add_reminder_button")
        ) {
            Icon(
                imageVector = Icons.Rounded.NotificationsActive,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.btn_add_reminder),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
