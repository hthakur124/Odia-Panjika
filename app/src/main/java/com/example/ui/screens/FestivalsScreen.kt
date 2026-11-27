package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.EventNote
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalendarDate
import com.example.data.FestivalFilter
import com.example.data.OdiaDayEntry
import com.example.data.OdiaFormatting
import com.example.ui.OdiaCalendarUiState
import com.example.ui.components.LunarPhaseDot
import com.example.ui.components.PattachitraHeaderContainer
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamOutline
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.TempleTealContainer
import com.example.ui.theme.TempleTealDark
import com.example.ui.theme.TempleTealSoft
import com.example.ui.theme.VermilionRed

@Composable
fun FestivalsScreen(
    uiState: OdiaCalendarUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (FestivalFilter) -> Unit,
    onEntryTapped: (CalendarDate) -> Unit,
    onAddReminder: (CalendarDate, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filtered = uiState.filteredFestivals
    val reminderDates = uiState.remindersByDate.keys

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        PattachitraHeaderContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.festivals_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.festivals_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SaffronHighlight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = SaffronGold
                    ) {
                        Text(
                            text = "${OdiaFormatting.toOdiaDigits(filtered.size)} ଟି",
                            style = MaterialTheme.typography.titleSmall,
                            color = InkPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
            ) {
                OutlinedTextField(
                    value = uiState.festivalSearchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_festivals_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = VermilionRed
                        )
                    },
                    trailingIcon = {
                        if (uiState.festivalSearchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChange("") },
                                modifier = Modifier.testTag("clear_festival_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = stringResource(R.string.cd_clear_search),
                                    tint = InkSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CreamSurface,
                        unfocusedContainerColor = CreamSurface,
                        focusedBorderColor = VermilionRed,
                        unfocusedBorderColor = CreamOutline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("festival_search_input")
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf(
                        FestivalFilter.ALL to R.string.filter_all,
                        FestivalFilter.HOLIDAYS to R.string.filter_holidays,
                        FestivalFilter.PURNIMA to R.string.filter_purnima,
                        FestivalFilter.AMAVASYA to R.string.filter_amavasya,
                        FestivalFilter.EKADASHI to R.string.filter_ekadashi,
                        FestivalFilter.SANKRANTI to R.string.filter_sankranti
                    )
                    items(filters) { (filter, labelRes) ->
                        val selected = uiState.selectedFestivalFilter == filter
                        FilterChip(
                            selected = selected,
                            onClick = { onFilterSelected(filter) },
                            label = {
                                Text(
                                    text = stringResource(labelRes),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = CreamSurface,
                                labelColor = InkPrimary,
                                selectedContainerColor = TempleTeal,
                                selectedLabelColor = PureWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = CreamOutline,
                                selectedBorderColor = TempleTeal
                            ),
                            modifier = Modifier.testTag("festival_filter_${filter.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (filtered.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EventNote,
                                contentDescription = null,
                                tint = SaffronGold,
                                modifier = Modifier.size(52.dp)
                            )
                            Text(
                                text = stringResource(R.string.empty_festivals_search),
                                style = MaterialTheme.typography.titleMedium,
                                color = InkSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = filtered,
                            key = { it.date.isoKey }
                        ) { entry ->
                            FestivalDirectoryCard(
                                entry = entry,
                                hasReminder = reminderDates.contains(entry.date.isoKey),
                                onClick = { onEntryTapped(entry.date) },
                                onAddReminder = {
                                    onAddReminder(
                                        entry.date,
                                        entry.festival.ifBlank { OdiaFormatting.formatDateOdiaFull(entry.date) }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FestivalDirectoryCard(
    entry: OdiaDayEntry,
    hasReminder: Boolean,
    onClick: () -> Unit,
    onAddReminder: () -> Unit
) {
    val isHolidayItem = entry.isFullMaroonHoliday || entry.isHoliday
    val badgeBg = if (isHolidayItem) VermilionRed else TempleTeal
    val badgeFg = PureWhite

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isHolidayItem) com.example.ui.theme.VermilionSoft.copy(alpha = 0.5f) else CreamSurface,
        border = BorderStroke(
            width = if (isHolidayItem) 1.3.dp else 1.dp,
            color = if (isHolidayItem) VermilionRed.copy(alpha = 0.5f) else CreamOutline
        ),
        shadowElevation = 1.5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("festival_dir_card_${entry.date.isoKey}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = badgeBg,
                modifier = Modifier.size(width = 58.dp, height = 58.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = OdiaFormatting.toOdiaDigits(entry.date.day),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg
                    )
                    Text(
                        text = OdiaFormatting.monthNameOdia(entry.date.month),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = badgeFg.copy(alpha = 0.92f)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (entry.isPurnima || entry.isAmavasya) {
                        LunarPhaseDot(isPurnima = entry.isPurnima, size = 9.dp)
                    }
                    Text(
                        text = entry.festival.ifBlank { stringResource(R.string.label_holiday_badge) },
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        color = if (isHolidayItem) VermilionRed else InkPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${OdiaFormatting.fullWeekdayOdia(entry.date.dayOfWeek)} • ${entry.tithi}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isHolidayItem) VermilionRed.copy(alpha = 0.85f) else InkSecondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isHolidayItem) {
                        MetaBadge(text = "ଛୁଟି", bg = VermilionRed, fg = PureWhite)
                    }
                    if (entry.nakshatra.isNotBlank()) {
                        MetaBadge(text = entry.nakshatra, bg = TempleTealContainer, fg = TempleTealDark)
                    }
                    if (entry.rashi.isNotBlank()) {
                        MetaBadge(text = entry.rashi, bg = SaffronContainer, fg = SaffronDeep)
                    }
                }
            }

            IconButton(
                onClick = onAddReminder,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (hasReminder) SaffronContainer else TempleTealSoft)
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

@Composable
private fun MetaBadge(text: String, bg: Color, fg: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
