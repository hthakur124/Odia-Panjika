package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.FestivalFilter
import com.example.ui.components.AddReminderDialog
import com.example.ui.components.DayDetailBottomSheet
import com.example.ui.screens.AboutCalendarScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.FestivalsScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.OdiaCalendarTheme
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.TempleTeal

enum class OdiaTab(
    val routeId: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    CALENDAR(
        routeId = "calendar",
        labelRes = R.string.nav_calendar,
        selectedIcon = Icons.Rounded.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    ),
    FESTIVALS(
        routeId = "festivals",
        labelRes = R.string.nav_festivals,
        selectedIcon = Icons.Rounded.Celebration,
        unselectedIcon = Icons.Outlined.Celebration
    ),
    REMINDERS(
        routeId = "reminders",
        labelRes = R.string.nav_reminders,
        selectedIcon = Icons.Rounded.NotificationsActive,
        unselectedIcon = Icons.Outlined.Notifications
    ),
    SETTINGS(
        routeId = "settings",
        labelRes = R.string.nav_settings,
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@Composable
fun OdiaCalendarApp(
    viewModel: OdiaCalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OdiaCalendarTheme(
        palette = uiState.themePalette,
        textSizeOption = uiState.textSizeOption
    ) {
        OdiaCalendarShell(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun OdiaCalendarShell(
    uiState: OdiaCalendarUiState,
    viewModel: OdiaCalendarViewModel
) {
    val colors = LocalOdiaColors.current
    var activeTab by rememberSaveable { mutableStateOf(OdiaTab.CALENDAR) }
    var showAboutUnderSettings by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showAboutUnderSettings || activeTab != OdiaTab.CALENDAR) {
        if (showAboutUnderSettings) {
            showAboutUnderSettings = false
        } else {
            activeTab = OdiaTab.CALENDAR
        }
    }

    val openHolidaysList: () -> Unit = {
        showAboutUnderSettings = false
        viewModel.selectFestivalFilter(FestivalFilter.HOLIDAYS)
        activeTab = OdiaTab.FESTIVALS
    }

    val onSelectTab: (OdiaTab) -> Unit = { tab ->
        if (tab != OdiaTab.SETTINGS) {
            showAboutUnderSettings = false
        }
        activeTab = tab
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        if (useNavigationRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    containerColor = colors.cardSurface,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    OdiaTab.entries.forEach { tab ->
                        val selected = activeTab == tab
                        NavigationRailItem(
                            selected = selected,
                            onClick = { onSelectTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = stringResource(tab.labelRes)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(tab.labelRes),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = colors.primaryAccent,
                                selectedTextColor = colors.primaryAccent,
                                indicatorColor = SaffronContainer,
                                unselectedIconColor = InkSecondary,
                                unselectedTextColor = InkSecondary
                            ),
                            modifier = Modifier.testTag("nav_rail_${tab.routeId}")
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    OdiaTabContent(
                        activeTab = activeTab,
                        showAboutUnderSettings = showAboutUnderSettings,
                        uiState = uiState,
                        viewModel = viewModel,
                        onOpenAbout = { showAboutUnderSettings = true },
                        onCloseAbout = { showAboutUnderSettings = false },
                        onOpenHolidaysList = openHolidaysList
                    )
                }
            }
        } else {
            Scaffold(
                containerColor = colors.creamBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    NavigationBar(
                        containerColor = colors.cardSurface,
                        tonalElevation = 4.dp
                    ) {
                        OdiaTab.entries.forEach { tab ->
                            val selected = activeTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { onSelectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = stringResource(tab.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(tab.labelRes),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colors.primaryAccent,
                                    selectedTextColor = colors.primaryAccent,
                                    indicatorColor = SaffronContainer,
                                    unselectedIconColor = InkSecondary,
                                    unselectedTextColor = InkSecondary
                                ),
                                modifier = Modifier.testTag("nav_bar_${tab.routeId}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                OdiaTabContent(
                    activeTab = activeTab,
                    showAboutUnderSettings = showAboutUnderSettings,
                    uiState = uiState,
                    viewModel = viewModel,
                    onOpenAbout = { showAboutUnderSettings = true },
                    onCloseAbout = { showAboutUnderSettings = false },
                    onOpenHolidaysList = openHolidaysList,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Day Panjika Modal Bottom Sheet
        if (uiState.showDayDetailSheet) {
            DayDetailBottomSheet(
                date = uiState.selectedDate,
                entry = uiState.selectedEntry,
                solarTimes = uiState.selectedSolarTimes,
                dayReminders = uiState.selectedDateReminders,
                onDismiss = viewModel::dismissDayDetailSheet,
                onPreviousDay = { viewModel.navigateDetailDay(-1) },
                onNextDay = { viewModel.navigateDetailDay(1) },
                onAddReminder = {
                    viewModel.openAddReminder(
                        date = uiState.selectedDate,
                        prefillTitle = uiState.selectedEntry?.festival.orEmpty()
                    )
                },
                onDeleteReminder = viewModel::deleteReminder
            )
        }

        // Add Reminder Modal Dialog
        uiState.reminderDraft?.let { draft ->
            AddReminderDialog(
                date = draft.date,
                initialTitle = draft.prefillTitle,
                onDismiss = viewModel::dismissAddReminder,
                onSave = { title, note, hour, minute ->
                    viewModel.saveReminder(
                        date = draft.date,
                        title = title,
                        note = note,
                        hour = hour,
                        minute = minute
                    )
                }
            )
        }
    }
}

@Composable
private fun OdiaTabContent(
    activeTab: OdiaTab,
    showAboutUnderSettings: Boolean,
    uiState: OdiaCalendarUiState,
    viewModel: OdiaCalendarViewModel,
    onOpenAbout: () -> Unit,
    onCloseAbout: () -> Unit,
    onOpenHolidaysList: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = TempleTeal)
        }
        return
    }

    when (activeTab) {
        OdiaTab.CALENDAR -> CalendarScreen(
            uiState = uiState,
            onPreviousMonth = viewModel::previousMonth,
            onNextMonth = viewModel::nextMonth,
            onSelectMonth = viewModel::selectMonth,
            onDateTapped = viewModel::onDateTapped,
            onAddReminderForDate = viewModel::openAddReminder,
            modifier = modifier
        )

        OdiaTab.FESTIVALS -> FestivalsScreen(
            uiState = uiState,
            onSearchQueryChange = viewModel::updateFestivalSearch,
            onFilterSelected = viewModel::selectFestivalFilter,
            onEntryTapped = viewModel::onDateTapped,
            onAddReminder = viewModel::openAddReminder,
            modifier = modifier
        )

        OdiaTab.REMINDERS -> RemindersScreen(
            uiState = uiState,
            onAddReminderClick = {
                viewModel.openAddReminder(
                    date = uiState.selectedDate,
                    prefillTitle = uiState.selectedEntry?.festival.orEmpty()
                )
            },
            onReminderCardClick = viewModel::onDateTapped,
            onToggleReminder = viewModel::toggleReminder,
            onDeleteReminder = viewModel::deleteReminder,
            modifier = modifier
        )

        OdiaTab.SETTINGS -> {
            if (showAboutUnderSettings) {
                AboutCalendarScreen(
                    uiState = uiState,
                    onBack = onCloseAbout,
                    onOpenHolidaysList = onOpenHolidaysList,
                    modifier = modifier
                )
            } else {
                SettingsScreen(
                    uiState = uiState,
                    onSelectTextSize = viewModel::setTextSizeOption,
                    onSelectThemePalette = viewModel::setThemePalette,
                    onOpenAboutCalendar = onOpenAbout,
                    modifier = modifier
                )
            }
        }
    }
}
