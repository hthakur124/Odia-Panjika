package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.ads.AdMobBannerAd
import com.example.ads.AdMobManager
import com.example.data.FestivalFilter
import com.example.ui.components.AddReminderDialog
import com.example.ui.components.DayDetailBottomSheet
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.FestivalsScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.OdiaCalendarTheme

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
        themePalette = uiState.themePalette,
        textSizeOption = uiState.textSizeOption
    ) {
        OdiaCalendarScaffoldContent(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun OdiaCalendarScaffoldContent(
    uiState: OdiaCalendarUiState,
    viewModel: OdiaCalendarViewModel
) {
    val colors = LocalOdiaColors.current
    val context = LocalContext.current
    val canRequestAds by AdMobManager.canRequestAds.collectAsStateWithLifecycle()
    val isPrivacyOptionsRequired by AdMobManager.isPrivacyOptionsRequired.collectAsStateWithLifecycle()
    var activeTab by rememberSaveable { mutableStateOf(OdiaTab.CALENDAR) }

    BackHandler(enabled = activeTab != OdiaTab.CALENDAR) {
        activeTab = OdiaTab.CALENDAR
    }

    val openHolidaysList: () -> Unit = {
        viewModel.selectFestivalFilter(FestivalFilter.HOLIDAYS)
        activeTab = OdiaTab.FESTIVALS
    }

    val openAdPrivacySettings: () -> Unit = {
        context.findActivity()?.let { activity ->
            AdMobManager.showPrivacyOptionsForm(activity)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        if (useNavigationRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    containerColor = colors.surface,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    OdiaTab.entries.forEach { tab ->
                        val selected = activeTab == tab
                        NavigationRailItem(
                            selected = selected,
                            onClick = { activeTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = stringResource(tab.labelRes)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(tab.labelRes),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = colors.primary,
                                selectedTextColor = colors.primary,
                                indicatorColor = colors.accentContainer,
                                unselectedIconColor = colors.inkSecondary,
                                unselectedTextColor = colors.inkSecondary
                            ),
                            modifier = Modifier.testTag("nav_rail_${tab.routeId}")
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Box(modifier = Modifier.weight(1f)) {
                        OdiaTabContent(
                            activeTab = activeTab,
                            uiState = uiState,
                            viewModel = viewModel,
                            onOpenHolidaysList = openHolidaysList,
                            isPrivacyOptionsRequired = isPrivacyOptionsRequired,
                            onOpenAdPrivacySettings = openAdPrivacySettings
                        )
                    }
                    AdMobBannerAd(canRequestAds = canRequestAds)
                }
            }
        } else {
            Scaffold(
                containerColor = colors.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    Column {
                        AdMobBannerAd(canRequestAds = canRequestAds)
                        if (canRequestAds) {
                            HorizontalDivider(
                                color = colors.outline.copy(alpha = 0.6f),
                                thickness = 0.8.dp
                            )
                        }
                        NavigationBar(
                            containerColor = colors.surface,
                            tonalElevation = 4.dp
                        ) {
                            OdiaTab.entries.forEach { tab ->
                                val selected = activeTab == tab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { activeTab = tab },
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
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = colors.primary,
                                        selectedTextColor = colors.primary,
                                        indicatorColor = colors.accentContainer,
                                        unselectedIconColor = colors.inkSecondary,
                                        unselectedTextColor = colors.inkSecondary
                                    ),
                                    modifier = Modifier.testTag("nav_bar_${tab.routeId}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                OdiaTabContent(
                    activeTab = activeTab,
                    uiState = uiState,
                    viewModel = viewModel,
                    onOpenHolidaysList = openHolidaysList,
                    isPrivacyOptionsRequired = isPrivacyOptionsRequired,
                    onOpenAdPrivacySettings = openAdPrivacySettings,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

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
    uiState: OdiaCalendarUiState,
    viewModel: OdiaCalendarViewModel,
    onOpenHolidaysList: () -> Unit,
    isPrivacyOptionsRequired: Boolean,
    onOpenAdPrivacySettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOdiaColors.current
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colors.secondary)
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

        OdiaTab.SETTINGS -> SettingsScreen(
            uiState = uiState,
            onSelectTextSize = viewModel::selectTextSize,
            onSelectThemePalette = viewModel::selectThemePalette,
            onOpenHolidaysList = onOpenHolidaysList,
            isPrivacyOptionsRequired = isPrivacyOptionsRequired,
            onOpenAdPrivacySettings = onOpenAdPrivacySettings,
            modifier = modifier
        )
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
