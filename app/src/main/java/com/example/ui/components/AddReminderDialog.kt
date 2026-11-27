package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.data.CalendarDate
import com.example.data.OdiaFormatting
import com.example.ui.theme.LocalOdiaColors

@Composable
fun AddReminderDialog(
    date: CalendarDate,
    initialTitle: String,
    onDismiss: () -> Unit,
    onSave: (title: String, note: String, hour: Int, minute: Int) -> Unit
) {
    val colors = LocalOdiaColors.current
    val context = LocalContext.current
    var title by remember(date, initialTitle) { mutableStateOf(initialTitle) }
    var note by remember(date) { mutableStateOf("") }
    var hour by remember(date) { mutableIntStateOf(7) }
    var minute by remember(date) { mutableIntStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Save reminder regardless of permission decision; notification posts when granted
        onSave(title, note, hour, minute)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = colors.secondary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.NotificationsActive,
                    contentDescription = null,
                    tint = colors.pureWhite,
                    modifier = Modifier.padding(11.dp)
                )
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.btn_add_reminder),
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colors.accentContainer,
                    border = BorderStroke(1.dp, colors.accent)
                ) {
                    Text(
                        text = "${OdiaFormatting.formatDateOdiaFull(date)} • ${OdiaFormatting.fullWeekdayOdia(date.dayOfWeek)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.accentDeep,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(R.string.reminder_title_label),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.reminder_title_placeholder),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.secondary,
                        unfocusedBorderColor = colors.outline,
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_title_input")
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = {
                        Text(
                            text = stringResource(R.string.reminder_note_label),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.reminder_note_placeholder),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    maxLines = 2,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.secondary,
                        unfocusedBorderColor = colors.outline,
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reminder_note_input")
                )

                Text(
                    text = stringResource(R.string.reminder_time_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.inkPrimary,
                    fontWeight = FontWeight.Bold
                )

                // Quick time preset chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        Triple(7, 0, R.string.reminder_morning),
                        Triple(12, 0, R.string.reminder_noon),
                        Triple(18, 0, R.string.reminder_evening)
                    )
                    presets.forEach { (presetHour, presetMin, labelRes) ->
                        val active = hour == presetHour && minute == presetMin
                        Surface(
                            onClick = {
                                hour = presetHour
                                minute = presetMin
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (active) colors.secondary else colors.surface,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (active) colors.secondary else colors.outline
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(labelRes),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (active) colors.pureWhite else colors.inkPrimary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Fine-tune time stepper row
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.secondarySoft,
                    border = BorderStroke(1.dp, colors.secondary.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                val total = ((hour * 60 + minute - 30) + 1440) % 1440
                                hour = total / 60
                                minute = total % 60
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Remove,
                                contentDescription = "Earlier",
                                tint = colors.secondary
                            )
                        }

                        Text(
                            text = OdiaFormatting.formatTime12HourOdia(hour, minute),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.inkPrimary
                        )

                        IconButton(
                            onClick = {
                                val total = (hour * 60 + minute + 30) % 1440
                                hour = total / 60
                                minute = total % 60
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Later",
                                tint = colors.secondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val granted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                        if (!granted) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            return@Button
                        }
                    }
                    onSave(title, note, hour, minute)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondary,
                    contentColor = colors.pureWhite
                ),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("save_reminder_button")
            ) {
                Text(
                    text = stringResource(R.string.btn_save_reminder),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.inkSecondary
                )
            }
        }
    )
}
