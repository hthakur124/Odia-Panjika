package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.OdiaTextSizeOption
import com.example.ui.theme.OdiaThemePalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private val Context.panjikaDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "odia_panjika_reminders_store"
)

data class AppearancePreferences(
    val textSizeOption: OdiaTextSizeOption = OdiaTextSizeOption.NORMAL,
    val themePalette: OdiaThemePalette = OdiaThemePalette.VERMILION_HERITAGE
)

class OdiaCalendarRepository(
    private val context: Context
) {

    suspend fun loadCalendarEntries(): List<OdiaDayEntry> = withContext(Dispatchers.IO) {
        val text = context.assets.open(BUNDLED_ASSET_NAME)
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
        CsvCalendarParser.parse(text)
    }

    val remindersFlow: Flow<List<CalendarReminder>> = context.panjikaDataStore.data.map { prefs ->
        decodeReminders(prefs[KEY_REMINDERS_JSON].orEmpty())
    }

    val appearanceFlow: Flow<AppearancePreferences> = context.panjikaDataStore.data.map { prefs ->
        AppearancePreferences(
            textSizeOption = OdiaTextSizeOption.fromKey(prefs[KEY_TEXT_SIZE]),
            themePalette = OdiaThemePalette.fromKey(prefs[KEY_THEME_PALETTE])
        )
    }

    suspend fun saveTextSize(option: OdiaTextSizeOption) {
        context.panjikaDataStore.edit { prefs ->
            prefs[KEY_TEXT_SIZE] = option.name
        }
    }

    suspend fun saveThemePalette(palette: OdiaThemePalette) {
        context.panjikaDataStore.edit { prefs ->
            prefs[KEY_THEME_PALETTE] = palette.name
        }
    }

    suspend fun saveReminder(reminder: CalendarReminder) {
        context.panjikaDataStore.edit { prefs ->
            val current = decodeReminders(prefs[KEY_REMINDERS_JSON].orEmpty()).toMutableList()
            val idx = current.indexOfFirst { it.id == reminder.id }
            if (idx >= 0) {
                current[idx] = reminder
            } else {
                current.add(reminder)
            }
            val sorted = current.sortedWith(
                compareBy<CalendarReminder> { it.dateIso }
                    .thenBy { it.hour }
                    .thenBy { it.minute }
            )
            prefs[KEY_REMINDERS_JSON] = encodeReminders(sorted)
        }
        ReminderScheduler.schedule(context, reminder)
    }

    suspend fun toggleReminder(reminderId: String) {
        var updatedReminder: CalendarReminder? = null
        context.panjikaDataStore.edit { prefs ->
            val current = decodeReminders(prefs[KEY_REMINDERS_JSON].orEmpty()).map { item ->
                if (item.id == reminderId) {
                    val toggled = item.copy(isEnabled = !item.isEnabled)
                    updatedReminder = toggled
                    toggled
                } else {
                    item
                }
            }
            prefs[KEY_REMINDERS_JSON] = encodeReminders(current)
        }
        updatedReminder?.let { ReminderScheduler.schedule(context, it) }
    }

    suspend fun deleteReminder(reminderId: String) {
        context.panjikaDataStore.edit { prefs ->
            val current = decodeReminders(prefs[KEY_REMINDERS_JSON].orEmpty())
                .filterNot { it.id == reminderId }
            prefs[KEY_REMINDERS_JSON] = encodeReminders(current)
        }
        ReminderScheduler.cancel(context, reminderId)
    }

    private fun encodeReminders(list: List<CalendarReminder>): String {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("dateIso", item.dateIso)
                put("title", item.title)
                put("note", item.note)
                put("hour", item.hour)
                put("minute", item.minute)
                put("isEnabled", item.isEnabled)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun decodeReminders(raw: String): List<CalendarReminder> {
        if (raw.isBlank()) return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList(array.length()) {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        CalendarReminder(
                            id = obj.optString("id"),
                            dateIso = obj.optString("dateIso"),
                            title = obj.optString("title"),
                            note = obj.optString("note"),
                            hour = obj.optInt("hour", 7),
                            minute = obj.optInt("minute", 0),
                            isEnabled = obj.optBoolean("isEnabled", true)
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    companion object {
        private const val BUNDLED_ASSET_NAME = "odia_calendar_2027.csv"
        private val KEY_REMINDERS_JSON = stringPreferencesKey("reminders_json")
        private val KEY_TEXT_SIZE = stringPreferencesKey("odia_text_size")
        private val KEY_THEME_PALETTE = stringPreferencesKey("odia_theme_palette")
    }
}
