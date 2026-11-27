package com.example.data

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

/**
 * Computes astronomical Sunrise and Sunset for Odisha (Bhubaneswar / Puri: 20.2961° N, 85.8245° E, IST +5:30)
 * using standard NOAA solar position equations. If the user's Google Sheet specifies explicit
 * Sunrise/Sunset columns, those values are used directly.
 */
object SolarCalculator {

    private const val ODISHA_LATITUDE = 20.2961
    private const val ODISHA_LONGITUDE = 85.8245
    private const val IST_OFFSET_MINUTES = 330.0 // UTC +5:30

    fun calculateForEntry(entry: OdiaDayEntry): SolarTimes {
        if (entry.sunriseOverride.isNotBlank() && entry.sunsetOverride.isNotBlank()) {
            return SolarTimes(
                sunriseEn = entry.sunriseOverride,
                sunsetEn = entry.sunsetOverride,
                sunriseOdia = OdiaFormatting.toOdiaDigits(entry.sunriseOverride),
                sunsetOdia = OdiaFormatting.toOdiaDigits(entry.sunsetOverride),
                daylightEn = "From Google Sheet",
                daylightOdia = "ଗୁଗୁଲ୍ ଶିଟ୍ ତଥ୍ୟ",
                isFromSheet = true
            )
        }
        return calculateForDate(entry.date)
    }

    fun calculateForDate(date: CalendarDate): SolarTimes {
        val dayOfYear = date.dayOfYear
        val gamma = (2.0 * PI / 365.0) * (dayOfYear - 1)

        // Equation of time (minutes)
        val eqTime = 229.18 * (
            0.000075 +
                0.001868 * cos(gamma) -
                0.032077 * sin(gamma) -
                0.014615 * cos(2.0 * gamma) -
                0.040849 * sin(2.0 * gamma)
            )

        // Solar declination angle (radians)
        val decl = 0.006918 -
            0.399912 * cos(gamma) +
            0.070257 * sin(gamma) -
            0.006758 * cos(2.0 * gamma) +
            0.000907 * sin(2.0 * gamma) -
            0.002697 * cos(3.0 * gamma) +
            0.00148 * sin(3.0 * gamma)

        val latRad = Math.toRadians(ODISHA_LATITUDE)
        val zenithRad = Math.toRadians(90.833) // Official sunrise/sunset zenith including atmospheric refraction

        val cosHa = (cos(zenithRad) / (cos(latRad) * cos(decl))) - (tan(latRad) * tan(decl))
        val clampedCosHa = cosHa.coerceIn(-1.0, 1.0)
        val haDegrees = Math.toDegrees(acos(clampedCosHa))

        // Solar noon in IST minutes from midnight
        val solarNoonMinutes = 720.0 - (4.0 * ODISHA_LONGITUDE) - eqTime + IST_OFFSET_MINUTES
        val sunriseMinutes = (solarNoonMinutes - haDegrees * 4.0).toInt().coerceIn(0, 1439)
        val sunsetMinutes = (solarNoonMinutes + haDegrees * 4.0).toInt().coerceIn(0, 1439)
        val daylightMinutes = (sunsetMinutes - sunriseMinutes).coerceAtLeast(0)

        val sunriseEn = formatMinutes12Hour(sunriseMinutes)
        val sunsetEn = formatMinutes12Hour(sunsetMinutes)
        val hours = daylightMinutes / 60
        val mins = daylightMinutes % 60

        val daylightEn = "${hours}h ${mins}m daylight"
        val daylightOdia = "${OdiaFormatting.toOdiaDigits(hours)} ଘଣ୍ଟା ${OdiaFormatting.toOdiaDigits(mins)} ମି."

        return SolarTimes(
            sunriseEn = sunriseEn,
            sunsetEn = sunsetEn,
            sunriseOdia = OdiaFormatting.toOdiaDigits(sunriseEn),
            sunsetOdia = OdiaFormatting.toOdiaDigits(sunsetEn),
            daylightEn = daylightEn,
            daylightOdia = daylightOdia,
            isFromSheet = false
        )
    }

    private fun formatMinutes12Hour(totalMinutes: Int): String {
        val hour24 = (totalMinutes / 60) % 24
        val minute = totalMinutes % 60
        val period = if (hour24 < 12) "AM" else "PM"
        val hour12 = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        return "%02d:%02d %s".format(hour12, minute, period)
    }
}
