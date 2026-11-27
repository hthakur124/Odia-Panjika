package com.example.data

import java.util.Calendar
import java.util.GregorianCalendar

/**
 * MinSDK 24-safe lightweight calendar date value object.
 */
data class CalendarDate(
    val year: Int,
    val month: Int, // 1..12
    val day: Int    // 1..31
) : Comparable<CalendarDate> {

    /**
     * 1 = Sunday, 2 = Monday, ..., 7 = Saturday
     */
    val dayOfWeek: Int
        get() {
            val cal = GregorianCalendar(year, month - 1, day)
            return cal.get(Calendar.DAY_OF_WEEK)
        }

    val dayOfYear: Int
        get() {
            val cal = GregorianCalendar(year, month - 1, day)
            return cal.get(Calendar.DAY_OF_YEAR)
        }

    val isoKey: String
        get() = "%04d-%02d-%02d".format(year, month, day)

    fun plusDays(days: Int): CalendarDate {
        val cal = GregorianCalendar(year, month - 1, day)
        cal.add(Calendar.DAY_OF_MONTH, days)
        return CalendarDate(
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun compareTo(other: CalendarDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        return day.compareTo(other.day)
    }

    companion object {
        fun daysInMonth(year: Int, month: Int): Int {
            val cal = GregorianCalendar(year, month - 1, 1)
            return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        fun firstWeekdayOffsetSundayStart(year: Int, month: Int): Int {
            val cal = GregorianCalendar(year, month - 1, 1)
            return cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
        }

        fun today(): CalendarDate {
            val cal = Calendar.getInstance()
            return CalendarDate(
                year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH) + 1,
                day = cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        fun fromIsoKey(isoKey: String): CalendarDate? {
            val parts = isoKey.split("-")
            if (parts.size != 3) return null
            val y = parts[0].toIntOrNull() ?: return null
            val m = parts[1].toIntOrNull() ?: return null
            val d = parts[2].toIntOrNull() ?: return null
            if (m !in 1..12 || d !in 1..31) return null
            return CalendarDate(y, m, d)
        }
    }
}

data class OdiaDayEntry(
    val date: CalendarDate,
    val rawDateText: String,
    val weekdayEnglish: String,
    val weekdayOdia: String,
    val isHoliday: Boolean,
    val isRedInPrint: Boolean,
    val tithi: String,
    val nakshatra: String,
    val rashi: String,
    val festival: String,
    val shubhaDin: String,
    val sunriseOverride: String = "",
    val sunsetOverride: String = ""
) {
    val isSunday: Boolean
        get() = date.dayOfWeek == Calendar.SUNDAY ||
            weekdayEnglish.equals("Sunday", ignoreCase = true) ||
            weekdayOdia == "ରବି"

    val hasFestival: Boolean
        get() = festival.isNotBlank()

    val hasShubhaDin: Boolean
        get() = shubhaDin.isNotBlank()

    /**
     * Full maroon background + bold white text for gazetted/print holidays.
     */
    val isFullMaroonHoliday: Boolean
        get() = isRedInPrint || (isHoliday && !isSunday)

    /**
     * Full vibrant saffron background for festival days (when not already a maroon holiday).
     */
    val isFullSaffronFestival: Boolean
        get() = hasFestival && !isFullMaroonHoliday

    val isPurnima: Boolean
        get() = tithi.contains("ପୂର୍ଣ୍ଣିମା")

    val isAmavasya: Boolean
        get() = tithi.contains("ଅମାବାସ୍ୟା")

    val isEkadashi: Boolean
        get() = tithi.contains("ଏକାଦଶୀ") || festival.contains("ଏକାଦଶୀ")

    val isSankranti: Boolean
        get() = festival.contains("ସଂକ୍ରାନ୍ତି")
}

data class SolarTimes(
    val sunriseEn: String,
    val sunsetEn: String,
    val sunriseOdia: String,
    val sunsetOdia: String,
    val daylightEn: String,
    val daylightOdia: String,
    val isFromSheet: Boolean = false
)

data class CalendarReminder(
    val id: String,
    val dateIso: String,
    val title: String,
    val note: String = "",
    val hour: Int = 7,
    val minute: Int = 0,
    val isEnabled: Boolean = true
) {
    val date: CalendarDate?
        get() = CalendarDate.fromIsoKey(dateIso)
}

enum class FestivalFilter {
    ALL,
    HOLIDAYS,
    PURNIMA,
    AMAVASYA,
    EKADASHI,
    SANKRANTI
}
