package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalendarDate
import com.example.data.OdiaDayEntry
import com.example.data.OdiaFormatting
import com.example.ui.OdiaCalendarUiState
import com.example.ui.components.PattachitraHeaderContainer
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamOutline
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.TempleTealSoft
import com.example.ui.theme.VermilionRed
import com.example.ui.theme.VermilionSoft

@Composable
fun CalendarScreen(
    uiState: OdiaCalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectMonth: (Int) -> Unit,
    onDateTapped: (CalendarDate) -> Unit,
    onAddReminderForDate: (CalendarDate, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val remindersByDate = uiState.remindersByDate

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // 1. Compact Curved Maroon Header (reduced top/bottom gaps so more content fits on screen)
        item {
            CalendarHeritageHeader(
                currentYear = uiState.currentYear,
                currentMonth = uiState.currentMonth,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onSelectMonth = onSelectMonth
            )
        }

        // 2. Square-Style Colorful Calendar Grid with compact spacing
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReferenceStyleMonthGrid(
                        year = uiState.currentYear,
                        month = uiState.currentMonth,
                        selectedDate = uiState.selectedDate,
                        entriesByKey = uiState.entriesByKey,
                        reminderDateKeys = remindersByDate.keys,
                        onDateTapped = onDateTapped
                    )

                    // Compact Color Legend
                    CalendarColorLegend()

                    // Upcoming / Month Festivals Section Title ("ଆଗାମୀ ପର୍ବ")
                    Text(
                        text = stringResource(R.string.section_upcoming_festivals),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 21.sp,
                        color = VermilionRed,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp, start = 2.dp)
                    )
                }
            }
        }

        // 3. Upcoming Festivals List — Holidays styled in Red, regular festivals in Teal
        val monthFestivals = uiState.currentMonthFestivals
        if (monthFestivals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CreamSurface,
                        border = BorderStroke(1.dp, CreamOutline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 680.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.empty_month_festivals),
                            style = MaterialTheme.typography.bodyLarge,
                            color = InkSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        } else {
            items(
                items = monthFestivals,
                key = { it.date.isoKey }
            ) { entry ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    UpcomingFestivalCard(
                        entry = entry,
                        hasReminder = remindersByDate.containsKey(entry.date.isoKey),
                        onClick = { onDateTapped(entry.date) },
                        onReminderClick = { onAddReminderForDate(entry.date, entry.festival) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 680.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarHeritageHeader(
    currentYear: Int,
    currentMonth: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectMonth: (Int) -> Unit
) {
    val monthRowState = rememberLazyListState()
    LaunchedEffect(currentMonth) {
        monthRowState.animateScrollToItem((currentMonth - 1).coerceIn(0, 11))
    }

    PattachitraHeaderContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 6.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = OdiaFormatting.sakabdaSamvatLine(currentYear, currentMonth),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        color = SaffronHighlight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${OdiaFormatting.monthNameOdia(currentMonth)} ${OdiaFormatting.toOdiaDigits(currentYear)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 30.sp,
                        lineHeight = 36.sp,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("calendar_month_title")
                    )
                    Text(
                        text = OdiaFormatting.odiaSolarSpan(currentMonth),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = PureWhite.copy(alpha = 0.92f),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onPreviousMonth,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PureWhite.copy(alpha = 0.16f))
                            .testTag("prev_month_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.cd_previous_month),
                            tint = PureWhite,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    IconButton(
                        onClick = onNextMonth,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PureWhite.copy(alpha = 0.16f))
                            .testTag("next_month_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.cd_next_month),
                            tint = PureWhite,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Compact Scrollable Month Pills
            LazyRow(
                state = monthRowState,
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(OdiaFormatting.monthsOdia) { index, odiaMonth ->
                    val monthNum = index + 1
                    val isSelected = monthNum == currentMonth
                    Surface(
                        onClick = { onSelectMonth(monthNum) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) SaffronGold else PureWhite.copy(alpha = 0.15f),
                        border = if (isSelected) {
                            BorderStroke(1.dp, PureWhite.copy(alpha = 0.85f))
                        } else {
                            BorderStroke(1.dp, PureWhite.copy(alpha = 0.22f))
                        },
                        modifier = Modifier.testTag("month_pill_$monthNum")
                    ) {
                        Text(
                            text = odiaMonth,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) InkPrimary else PureWhite,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReferenceStyleMonthGrid(
    year: Int,
    month: Int,
    selectedDate: CalendarDate,
    entriesByKey: Map<String, OdiaDayEntry>,
    reminderDateKeys: Set<String>,
    onDateTapped: (CalendarDate) -> Unit
) {
    val firstDayOffset = CalendarDate.firstWeekdayOffsetSundayStart(year, month)
    val daysInMonth = CalendarDate.daysInMonth(year, month)
    val totalCells = ((firstDayOffset + daysInMonth + 6) / 7) * 7
    val weeks = totalCells / 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("calendar_month_grid_card"),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Bold Maroon Weekday Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            OdiaFormatting.weekdayHeadersOdia.forEach { odiaDay ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = odiaDay,
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = VermilionRed
                    )
                }
            }
        }

        // Crisp Square-Style Date Tiles
        for (week in 0 until weeks) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (col in 0 until 7) {
                    val cellIndex = week * 7 + col
                    val dayNum = cellIndex - firstDayOffset + 1
                    if (dayNum in 1..daysInMonth) {
                        val cellDate = CalendarDate(year, month, dayNum)
                        val entry = entriesByKey[cellDate.isoKey]
                        val isSelected = cellDate == selectedDate
                        val isSunday = col == 0
                        val hasReminder = reminderDateKeys.contains(cellDate.isoKey)

                        CalendarSquareDayTile(
                            date = cellDate,
                            entry = entry,
                            isSunday = isSunday,
                            isSelected = isSelected,
                            hasReminder = hasReminder,
                            onClick = { onDateTapped(cellDate) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarSquareDayTile(
    date: CalendarDate,
    entry: OdiaDayEntry?,
    isSunday: Boolean,
    isSelected: Boolean,
    hasReminder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFullMaroonHoliday = entry?.isFullMaroonHoliday == true
    val isFullSaffronFestival = entry?.isFullSaffronFestival == true

    val targetBg = when {
        isFullMaroonHoliday -> VermilionRed
        isFullSaffronFestival -> SaffronGold
        else -> CreamSurface
    }
    val animatedBg by animateColorAsState(targetValue = targetBg, label = "square_tile_bg")

    val numeralColor = when {
        isFullMaroonHoliday -> PureWhite
        isSunday -> VermilionRed
        else -> InkPrimary
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "square_tile_scale"
    )

    // Crisp square tile styling (1:1 aspect ratio with refined 8.dp corners)
    val squareShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .shadow(
                elevation = if (isFullMaroonHoliday || isFullSaffronFestival) 2.5.dp else 1.dp,
                shape = squareShape,
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            )
            .clip(squareShape)
            .background(animatedBg)
            .then(
                if (isSelected) {
                    Modifier.border(BorderStroke(2.5.dp, TempleTeal), squareShape)
                } else if (!isFullMaroonHoliday && !isFullSaffronFestival) {
                    Modifier.border(BorderStroke(0.7.dp, CreamOutline.copy(alpha = 0.75f)), squareShape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .testTag("calendar_day_${date.isoKey}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = OdiaFormatting.toOdiaDigits(date.day),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = numeralColor
        )

        if (isFullMaroonHoliday && entry?.hasFestival == true) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
                    .size(4.5.dp)
                    .clip(CircleShape)
                    .background(SaffronHighlight)
            )
        }

        if (hasReminder) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (isFullMaroonHoliday) SaffronGold else TempleTeal)
            )
        }
    }
}

@Composable
private fun CalendarColorLegend() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CreamSurface,
        border = BorderStroke(1.dp, CreamOutline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendSwatch(bgColor = VermilionRed, label = stringResource(R.string.legend_holiday))
            LegendSwatch(bgColor = SaffronGold, label = stringResource(R.string.legend_festival))
            LegendTextSwatch(textColor = VermilionRed, label = stringResource(R.string.legend_sunday))
            LegendSwatch(bgColor = TempleTeal, label = stringResource(R.string.legend_reminder))
        }
    }
}

@Composable
private fun LegendSwatch(bgColor: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(bgColor)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = InkPrimary
        )
    }
}

@Composable
private fun LegendTextSwatch(textColor: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CreamSurface)
                .border(1.5.dp, textColor, RoundedCornerShape(3.dp))
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/**
 * Upcoming Festival Card ("ଆଗାମୀ ପର୍ବ"):
 * - Holidays are prominently styled in Red (full maroon/red square date badge + red holiday title/tag)
 * - Non-holiday festivals feature the vibrant Teal square date badge
 */
@Composable
private fun UpcomingFestivalCard(
    entry: OdiaDayEntry,
    hasReminder: Boolean,
    onClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHolidayItem = entry.isFullMaroonHoliday || entry.isHoliday

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isHolidayItem) VermilionSoft.copy(alpha = 0.5f) else CreamSurface,
        shadowElevation = 1.5.dp,
        border = BorderStroke(
            width = if (isHolidayItem) 1.3.dp else 1.dp,
            color = if (isHolidayItem) VermilionRed.copy(alpha = 0.5f) else CreamOutline.copy(alpha = 0.75f)
        ),
        modifier = modifier.testTag("month_festival_card_${entry.date.isoKey}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Square Date Badge: Red for Holidays, Teal for regular festivals
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isHolidayItem) VermilionRed else TempleTeal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = OdiaFormatting.toOdiaDigits(entry.date.day),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = entry.festival,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        color = if (isHolidayItem) VermilionRed else InkPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isHolidayItem) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = VermilionRed
                        ) {
                            Text(
                                text = "ଛୁଟି",
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "${OdiaFormatting.fullWeekdayOdia(entry.date.dayOfWeek)} • ${entry.tithi}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = if (isHolidayItem) VermilionRed.copy(alpha = 0.85f) else InkSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = onReminderClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (hasReminder) SaffronContainer else TempleTealSoft)
                    .testTag("festival_reminder_btn_${entry.date.isoKey}")
            ) {
                Icon(
                    imageVector = if (hasReminder) {
                        Icons.Rounded.NotificationsActive
                    } else {
                        Icons.Rounded.NotificationsNone
                    },
                    contentDescription = stringResource(R.string.cd_add_reminder),
                    tint = if (hasReminder) VermilionRed else TempleTeal,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}
