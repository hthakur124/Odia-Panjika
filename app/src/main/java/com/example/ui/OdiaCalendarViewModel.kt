package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CalendarDate
import com.example.data.CalendarReminder
import com.example.data.FestivalFilter
import com.example.data.OdiaCalendarRepository
import com.example.data.OdiaDayEntry
import com.example.data.OdiaFormatting
import com.example.data.SolarCalculator
import com.example.data.SolarTimes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ReminderDraft(
    val date: CalendarDate,
    val prefillTitle: String = ""
)

data class OdiaCalendarUiState(
    val isLoading: Boolean = true,
    val allEntries: List<OdiaDayEntry> = emptyList(),
    val entriesByKey: Map<String, OdiaDayEntry> = emptyMap(),
    val currentYear: Int = 2027,
    val currentMonth: Int = 1,
    val selectedDate: CalendarDate = CalendarDate(2027, 1, 14),
    val showDayDetailSheet: Boolean = false,
    val festivalSearchQuery: String = "",
    val selectedFestivalFilter: FestivalFilter = FestivalFilter.ALL,
    val reminders: List<CalendarReminder> = emptyList(),
    val reminderDraft: ReminderDraft? = null
) {
    val selectedEntry: OdiaDayEntry?
        get() = entriesByKey[selectedDate.isoKey]

    val selectedSolarTimes: SolarTimes
        get() = selectedEntry?.let { SolarCalculator.calculateForEntry(it) }
            ?: SolarCalculator.calculateForDate(selectedDate)

    val remindersByDate: Map<String, List<CalendarReminder>>
        get() = reminders.groupBy { it.dateIso }

    val selectedDateReminders: List<CalendarReminder>
        get() = reminders.filter { it.dateIso == selectedDate.isoKey }

    val currentMonthEntries: List<OdiaDayEntry>
        get() = allEntries.filter { it.date.year == currentYear && it.date.month == currentMonth }

    val currentMonthFestivals: List<OdiaDayEntry>
        get() = currentMonthEntries.filter { it.hasFestival }

    val filteredFestivals: List<OdiaDayEntry>
        get() {
            val base = when (selectedFestivalFilter) {
                FestivalFilter.ALL -> allEntries.filter { it.hasFestival || it.isFullMaroonHoliday }
                FestivalFilter.HOLIDAYS -> allEntries.filter { it.isFullMaroonHoliday || (it.isSunday && it.hasFestival) }
                FestivalFilter.PURNIMA -> allEntries.filter { it.isPurnima }
                FestivalFilter.AMAVASYA -> allEntries.filter { it.isAmavasya }
                FestivalFilter.EKADASHI -> allEntries.filter { it.isEkadashi }
                FestivalFilter.SANKRANTI -> allEntries.filter { it.isSankranti }
            }
            val query = festivalSearchQuery.trim()
            if (query.isEmpty()) return base
            return base.filter { entry ->
                entry.festival.contains(query, ignoreCase = true) ||
                    entry.tithi.contains(query, ignoreCase = true) ||
                    entry.nakshatra.contains(query, ignoreCase = true) ||
                    entry.rashi.contains(query, ignoreCase = true) ||
                    entry.rawDateText.contains(query, ignoreCase = true) ||
                    OdiaFormatting.monthNameOdia(entry.date.month).contains(query, ignoreCase = true) ||
                    OdiaFormatting.monthNameEnglish(entry.date.month).contains(query, ignoreCase = true)
            }
        }
}

class OdiaCalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OdiaCalendarRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(OdiaCalendarUiState())
    val uiState: StateFlow<OdiaCalendarUiState> = _uiState.asStateFlow()

    init {
        loadCalendarData()
        observeReminders()
    }

    private fun loadCalendarData() {
        viewModelScope.launch {
            val entries = repository.loadCalendarEntries()
            val byKey = entries.associateBy { it.date.isoKey }
            val today = CalendarDate.today()
            val initialDate = when {
                byKey.containsKey(today.isoKey) -> today
                byKey.containsKey("2027-01-14") -> CalendarDate(2027, 1, 14)
                entries.isNotEmpty() -> entries.first().date
                else -> CalendarDate(2027, 1, 1)
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    allEntries = entries,
                    entriesByKey = byKey,
                    currentYear = initialDate.year,
                    currentMonth = initialDate.month,
                    selectedDate = initialDate
                )
            }
        }
    }

    private fun observeReminders() {
        viewModelScope.launch {
            repository.remindersFlow.collect { list ->
                _uiState.update { it.copy(reminders = list) }
            }
        }
    }

    fun selectMonth(month: Int) {
        val clamped = month.coerceIn(1, 12)
        val year = _uiState.value.currentYear
        val firstFestival = _uiState.value.allEntries.firstOrNull {
            it.date.year == year && it.date.month == clamped && it.hasFestival
        }?.date ?: CalendarDate(year, clamped, 1)

        _uiState.update {
            it.copy(
                currentMonth = clamped,
                selectedDate = firstFestival
            )
        }
    }

    fun previousMonth() {
        val current = _uiState.value.currentMonth
        selectMonth(if (current > 1) current - 1 else 12)
    }

    fun nextMonth() {
        val current = _uiState.value.currentMonth
        selectMonth(if (current < 12) current + 1 else 1)
    }

    fun onDateTapped(date: CalendarDate) {
        _uiState.update {
            it.copy(
                currentYear = date.year,
                currentMonth = date.month,
                selectedDate = date,
                showDayDetailSheet = true
            )
        }
    }

    fun dismissDayDetailSheet() {
        _uiState.update { it.copy(showDayDetailSheet = false) }
    }

    fun navigateDetailDay(deltaDays: Int) {
        val state = _uiState.value
        val target = state.selectedDate.plusDays(deltaDays)
        if (state.entriesByKey.containsKey(target.isoKey)) {
            _uiState.update {
                it.copy(
                    currentYear = target.year,
                    currentMonth = target.month,
                    selectedDate = target
                )
            }
        }
    }

    fun updateFestivalSearch(query: String) {
        _uiState.update { it.copy(festivalSearchQuery = query) }
    }

    fun selectFestivalFilter(filter: FestivalFilter) {
        _uiState.update { it.copy(selectedFestivalFilter = filter) }
    }

    fun openAddReminder(date: CalendarDate, prefillTitle: String = "") {
        val resolvedTitle = prefillTitle.ifBlank {
            _uiState.value.entriesByKey[date.isoKey]?.festival.orEmpty()
        }
        _uiState.update {
            it.copy(reminderDraft = ReminderDraft(date = date, prefillTitle = resolvedTitle))
        }
    }

    fun dismissAddReminder() {
        _uiState.update { it.copy(reminderDraft = null) }
    }

    fun saveReminder(date: CalendarDate, title: String, note: String, hour: Int, minute: Int) {
        val cleanTitle = title.trim().ifBlank { OdiaFormatting.formatDateOdiaFull(date) }
        val reminder = CalendarReminder(
            id = UUID.randomUUID().toString(),
            dateIso = date.isoKey,
            title = cleanTitle,
            note = note.trim(),
            hour = hour.coerceIn(0, 23),
            minute = minute.coerceIn(0, 59),
            isEnabled = true
        )
        viewModelScope.launch {
            repository.saveReminder(reminder)
            _uiState.update { it.copy(reminderDraft = null) }
        }
    }

    fun toggleReminder(reminderId: String) {
        viewModelScope.launch {
            repository.toggleReminder(reminderId)
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            repository.deleteReminder(reminderId)
        }
    }
}
