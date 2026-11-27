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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalendarDate
import com.example.data.CalendarReminder
import com.example.data.OdiaFormatting
import com.example.ui.OdiaCalendarUiState
import com.example.ui.components.PattachitraHeaderContainer
import com.example.ui.theme.LocalOdiaColors

@Composable
fun RemindersScreen(
    uiState: OdiaCalendarUiState,
    onAddReminderClick: () -> Unit,
    onReminderCardClick: (CalendarDate) -> Unit,
    onToggleReminder: (String) -> Unit,
    onDeleteReminder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    val reminders = uiState.reminders

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                                text = stringResource(R.string.reminders_title),
                                style = MaterialTheme.typography.headlineMedium,
                                color = colors.pureWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.reminders_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.accentHighlight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.accent
                        ) {
                            Text(
                                text = "${OdiaFormatting.toOdiaDigits(reminders.size)} ଟି",
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.inkPrimary,
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
                if (reminders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 680.dp)
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(22.dp),
                                color = colors.secondarySoft,
                                modifier = Modifier.size(84.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Rounded.NotificationsNone,
                                        contentDescription = null,
                                        tint = colors.secondary,
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                            }
                            Text(
                                text = stringResource(R.string.empty_reminders_title),
                                style = MaterialTheme.typography.headlineSmall,
                                color = colors.inkPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.empty_reminders_body),
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.inkSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 680.dp),
                        contentPadding = PaddingValues(
                            start = 14.dp,
                            end = 14.dp,
                            top = 14.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = reminders,
                            key = { it.id }
                        ) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                onCardClick = {
                                    reminder.date?.let { onReminderCardClick(it) }
                                },
                                onToggle = { onToggleReminder(reminder.id) },
                                onDelete = { onDeleteReminder(reminder.id) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddReminderClick,
            containerColor = colors.secondary,
            contentColor = colors.pureWhite,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_reminder_fab")
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.cd_add_reminder),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: CalendarReminder,
    onCardClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalOdiaColors.current
    val date = reminder.date

    Surface(
        onClick = onCardClick,
        shape = RoundedCornerShape(20.dp),
        color = colors.surface,
        border = BorderStroke(
            width = 1.2.dp,
            color = if (reminder.isEnabled) colors.secondary.copy(alpha = 0.5f) else colors.outline
        ),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reminder_card_${reminder.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (date != null) {
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = if (reminder.isEnabled) colors.secondary else colors.outline,
                    modifier = Modifier.size(width = 60.dp, height = 60.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = OdiaFormatting.toOdiaDigits(date.day),
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (reminder.isEnabled) colors.pureWhite else colors.inkSecondary
                        )
                        Text(
                            text = OdiaFormatting.monthNameOdia(date.month),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = if (reminder.isEnabled) colors.pureWhite.copy(alpha = 0.9f) else colors.inkSecondary
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colors.accentContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = colors.accentDeep,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = OdiaFormatting.formatTime12HourOdia(reminder.hour, reminder.minute),
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.accentDeep,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (reminder.note.isNotBlank()) {
                    Text(
                        text = reminder.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkSecondary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Switch(
                    checked = reminder.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.pureWhite,
                        checkedTrackColor = colors.secondary
                    )
                )
                IconButton(
                    onClick = onDelete,
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
