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
import androidx.compose.foundation.layout.width
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
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.LocalOdiaTextScale
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.TempleTealSoft

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
    val colors = LocalOdiaColors.current
    val remindersByDate = uiState.remindersByDate

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.creamBackground),
        contentPadding = PaddingValues(bottom = 22.dp)
    ) {
        // 1. Compact Curved Theme Header
        item {
            CalendarHeritageHeader(
                currentYear = uiState.currentYear,
                currentMonth = uiState.currentMonth,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onSelectMonth = onSelectMonth
            )
        }

        // 2. Traditional Odia Calendar Grid: Weekdays Vertically on Left + Big Square Date Tiles
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerticalWeekdayMonthGrid(
                        year = uiState.currentYear,
                        month = uiState.currentMonth,
                        selectedDate = uiState.selectedDate,
                        entriesByKey = uiState.entriesByKey,
                        reminderDateKeys = remindersByDate.keys,
                        onDateTapped = onDateTapped
                    )

                    CalendarColorLegend()

                    // Upcoming Festivals Section Heading ("ଆଗାମୀ ପର୍ବ")
                    Text(
                        text = stringResource(R.string.section_upcoming_festivals),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.primaryAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp, start = 2.dp)
                    )
                }
            }
        }

        // 3. Upcoming Festivals List — Holidays highlighted in Red, others in Teal
        val monthFestivals = uiState.currentMonthFestivals
        if (monthFestivals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = colors.cardSurface,
                        border = BorderStroke(1.dp, colors.outline),
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
                        .padding(horizontal = 10.dp, vertical = 4.dp),
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
    val textScale = LocalOdiaTextScale.current
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
                        fontSize = (13f * textScale).coerceAtMost(17f).sp,
                        color = SaffronHighlight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${OdiaFormatting.monthNameOdia(currentMonth)} ${OdiaFormatting.toOdiaDigits(currentYear)}",
                        fontSize = (28f * textScale).coerceAtMost(36f).sp,
                        lineHeight = (34f * textScale).coerceAtMost(42f).sp,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("calendar_month_title")
                    )
                    Text(
                        text = OdiaFormatting.odiaSolarSpan(currentMonth),
                        fontSize = (14f * textScale).coerceAtMost(18f).sp,
                        color = PureWhite.copy(alpha = 0.94f),
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
                            fontSize = (13f * textScale).coerceAtMost(17f).sp,
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

/**
 * Authentic Traditional Odia Wall Panjika Layout:
 * 7 weekday rows (ରବି to ଶନି) stacked VERTICALLY on the left side,
 * with 5 or 6 week columns of big, easy-to-read square date tiles extending to the right!
 */
@Composable
private fun VerticalWeekdayMonthGrid(
    year: Int,
    month: Int,
    selectedDate: CalendarDate,
    entriesByKey: Map<String, OdiaDayEntry>,
    reminderDateKeys: Set<String>,
    onDateTapped: (CalendarDate) -> Unit
) {
    val colors = LocalOdiaColors.current
    val textScale = LocalOdiaTextScale.current

    val firstDayOffset = CalendarDate.firstWeekdayOffsetSundayStart(year, month)
    val daysInMonth = CalendarDate.daysInMonth(year, month)
    val numWeeks = ((firstDayOffset + daysInMonth) + 6) / 7 // Always 5 or 6 columns

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("calendar_month_grid_card"),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // 7 vertical weekday rows: 0 = ରବି (Sunday) .. 6 = ଶନି (Saturday)
        for (weekdayRow in 0 until 7) {
            val isSundayRow = weekdayRow == 0
            val odiaDayName = OdiaFormatting.weekdayHeadersOdia[weekdayRow]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left-Side Vertical Weekday Label Tile (Square-proportioned pill)
                Box(
                    modifier = Modifier
                        .width(62.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSundayRow) colors.holidayRed else colors.primarySoft
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSundayRow) colors.holidayRed else colors.primaryAccent.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = odiaDayName,
                        fontSize = (16f * textScale).coerceAtMost(20f).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSundayRow) PureWhite else colors.primaryAccent,
                        maxLines = 1
                    )
                }

                // 5 or 6 Week Columns of Big Square Date Tiles
                for (weekCol in 0 until numWeeks) {
                    val dayNum = weekCol * 7 + weekdayRow - firstDayOffset + 1
                    if (dayNum in 1..daysInMonth) {
                        val cellDate = CalendarDate(year, month, dayNum)
                        val entry = entriesByKey[cellDate.isoKey]
                        val isSelected = cellDate == selectedDate
                        val hasReminder = reminderDateKeys.contains(cellDate.isoKey)

                        CalendarSquareDayTile(
                            date = cellDate,
                            entry = entry,
                            isSunday = isSundayRow,
                            isSelected = isSelected,
                            hasReminder = hasReminder,
                            onClick = { onDateTapped(cellDate) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.cardSurface.copy(alpha = 0.35f))
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
    val colors = LocalOdiaColors.current
    val textScale = LocalOdiaTextScale.current

    val isFullMaroonHoliday = entry?.isFullMaroonHoliday == true
    val isFullSaffronFestival = entry?.isFullSaffronFestival == true

    val targetBg = when {
        isFullMaroonHoliday -> colors.holidayRed
        isFullSaffronFestival -> colors.festivalSaffron
        else -> colors.cardSurface
    }
    val animatedBg by animateColorAsState(targetValue = targetBg, label = "square_tile_bg")

    val numeralColor = when {
        isFullMaroonHoliday -> PureWhite
        isSunday -> colors.holidayRed
        else -> InkPrimary
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "square_tile_scale"
    )

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
                    Modifier.border(BorderStroke(2.5.dp, colors.secondaryAccent), squareShape)
                } else if (!isFullMaroonHoliday && !isFullSaffronFestival) {
                    Modifier.border(BorderStroke(0.8.dp, colors.outline), squareShape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .testTag("calendar_day_${date.isoKey}"),
        contentAlignment = Alignment.Center
    ) {
        // Extra-large, bold, crystal-clear Odia numeral scaled by user text setting
        Text(
            text = OdiaFormatting.toOdiaDigits(date.day),
            fontSize = (23f * textScale).coerceAtMost(31f).sp,
            fontWeight = FontWeight.Bold,
            color = numeralColor,
            maxLines = 1
        )

        if (isFullMaroonHoliday && entry?.hasFestival == true) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 3.dp)
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(SaffronHighlight)
            )
        }

        if (hasReminder) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 3.dp, end = 3.dp)
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (isFullMaroonHoliday) SaffronGold else TempleTeal)
            )
        }
    }
}

@Composable
private fun CalendarColorLegend() {
    val colors = LocalOdiaColors.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendSwatch(bgColor = colors.holidayRed, label = stringResource(R.string.legend_holiday))
            LegendSwatch(bgColor = colors.festivalSaffron, label = stringResource(R.string.legend_festival))
            LegendTextSwatch(textColor = colors.holidayRed, label = stringResource(R.string.legend_sunday))
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
    val colors = LocalOdiaColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(colors.cardSurface)
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

@Composable
private fun UpcomingFestivalCard(
    entry: OdiaDayEntry,
    hasReminder: Boolean,
    onClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    val isHolidayItem = entry.isFullMaroonHoliday || entry.isHoliday

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isHolidayItem) colors.holidaySoft.copy(alpha = 0.55f) else colors.cardSurface,
        shadowElevation = 1.5.dp,
        border = BorderStroke(
            width = if (isHolidayItem) 1.3.dp else 1.dp,
            color = if (isHolidayItem) colors.holidayRed.copy(alpha = 0.55f) else colors.outline
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
            // Square Date Badge: Red for Holidays, Teal for other festivals
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isHolidayItem) colors.holidayRed else TempleTeal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = OdiaFormatting.toOdiaDigits(entry.date.day),
                    style = MaterialTheme.typography.headlineSmall,
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
                        color = if (isHolidayItem) colors.holidayRed else InkPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isHolidayItem) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.holidayRed
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
                    color = if (isHolidayItem) colors.holidayRed.copy(alpha = 0.85f) else InkSecondary,
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
                    tint = if (hasReminder) colors.holidayRed else TempleTeal,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}
