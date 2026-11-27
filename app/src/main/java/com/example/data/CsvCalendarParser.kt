package com.example.data

import java.util.Locale

/**
 * RFC-4180 compliant CSV parser tailored to the Odia Calendar Google Sheet schema.
 * Supports quoted fields with embedded commas, UTF-8 BOM, and flexible column matching.
 */
object CsvCalendarParser {

    private val monthAbbrMap = mapOf(
        "jan" to 1, "january" to 1,
        "feb" to 2, "february" to 2,
        "mar" to 3, "march" to 3,
        "apr" to 4, "april" to 4,
        "may" to 5,
        "jun" to 6, "june" to 6,
        "jul" to 7, "july" to 7,
        "aug" to 8, "august" to 8,
        "sep" to 9, "sept" to 9, "september" to 9,
        "oct" to 10, "october" to 10,
        "nov" to 11, "november" to 11,
        "dec" to 12, "december" to 12
    )

    fun parse(csvContent: String): List<OdiaDayEntry> {
        val cleaned = csvContent.removePrefix("\uFEFF")
        val rows = parseCsvRows(cleaned)
        if (rows.isEmpty()) return emptyList()

        val header = rows.first().map { it.trim().lowercase(Locale.US) }

        fun findColumn(vararg keywords: String, fallbackIndex: Int): Int {
            val found = header.indexOfFirst { col ->
                keywords.any { kw -> col.contains(kw) }
            }
            return if (found >= 0) found else fallbackIndex
        }

        val dateCol = findColumn("date", fallbackIndex = 0)
        val weekdayCol = findColumn("weekday", "day", fallbackIndex = 1)
        val baraCol = findColumn("ବାର", "bara", "odia day", fallbackIndex = 2)
        val chutiCol = findColumn("chuti", "holiday", fallbackIndex = 3)
        val redPrintCol = findColumn("red", "print", fallbackIndex = 4)
        val tithiCol = findColumn("tithi", "ତିଥି", fallbackIndex = 5)
        val nakshatraCol = findColumn("nakshatra", "ନକ୍ଷତ୍ର", fallbackIndex = 6)
        val rashiCol = findColumn("rashi", "ରାଶି", fallbackIndex = 7)
        val festivalCol = findColumn("parba", "festival", "ପର୍ବ", fallbackIndex = 8)
        val shubhaCol = findColumn("shubha", "ଶୁଭ", fallbackIndex = 9)
        val sunriseCol = header.indexOfFirst { it.contains("sunrise") || it.contains("ସୂର୍ଯ୍ୟୋଦୟ") }
        val sunsetCol = header.indexOfFirst { it.contains("sunset") || it.contains("ସୂର୍ଯ୍ୟାସ୍ତ") }

        val entries = mutableListOf<OdiaDayEntry>()
        for (i in 1 until rows.size) {
            val row = rows[i]
            if (row.all { it.isBlank() }) continue

            val rawDate = row.getOrNull(dateCol)?.trim().orEmpty()
            val parsedDate = parseDate(rawDate) ?: continue

            val weekdayEn = row.getOrNull(weekdayCol)?.trim().orEmpty()
            val weekdayOdia = row.getOrNull(baraCol)?.trim().orEmpty().ifBlank {
                OdiaFormatting.weekdayHeadersOdia.getOrElse(parsedDate.dayOfWeek - 1) { "" }
            }
            val chutiRaw = row.getOrNull(chutiCol)?.trim().orEmpty()
            val isHoliday = chutiRaw.equals("yes", ignoreCase = true) ||
                chutiRaw.equals("true", ignoreCase = true) ||
                chutiRaw.equals("y", ignoreCase = true) ||
                chutiRaw == "ହଁ"

            val redRaw = row.getOrNull(redPrintCol)?.trim().orEmpty()
            val isRedInPrint = redRaw.equals("y", ignoreCase = true) ||
                redRaw.equals("yes", ignoreCase = true) ||
                redRaw.equals("true", ignoreCase = true)

            val tithi = row.getOrNull(tithiCol)?.trim().orEmpty()
            val nakshatra = row.getOrNull(nakshatraCol)?.trim().orEmpty()
            val rashi = row.getOrNull(rashiCol)?.trim().orEmpty()
            val festival = row.getOrNull(festivalCol)?.trim().orEmpty()
            val shubhaDin = row.getOrNull(shubhaCol)?.trim().orEmpty()
            val sunriseOverride = if (sunriseCol >= 0) row.getOrNull(sunriseCol)?.trim().orEmpty() else ""
            val sunsetOverride = if (sunsetCol >= 0) row.getOrNull(sunsetCol)?.trim().orEmpty() else ""

            entries.add(
                OdiaDayEntry(
                    date = parsedDate,
                    rawDateText = rawDate,
                    weekdayEnglish = weekdayEn,
                    weekdayOdia = weekdayOdia,
                    isHoliday = isHoliday,
                    isRedInPrint = isRedInPrint,
                    tithi = tithi,
                    nakshatra = nakshatra,
                    rashi = rashi,
                    festival = festival,
                    shubhaDin = shubhaDin,
                    sunriseOverride = sunriseOverride,
                    sunsetOverride = sunsetOverride
                )
            )
        }
        return entries.sortedBy { it.date }
    }

    internal fun parseDate(raw: String): CalendarDate? {
        val text = raw.trim()
        if (text.isEmpty()) return null

        // Format 1: dd-MMM-yyyy (e.g., 01-Jan-2027)
        val dashParts = text.split("-")
        if (dashParts.size == 3) {
            val firstNum = dashParts[0].toIntOrNull()
            val secondMonth = monthAbbrMap[dashParts[1].lowercase(Locale.US)]
            val thirdNum = dashParts[2].toIntOrNull()
            if (firstNum != null && secondMonth != null && thirdNum != null) {
                return CalendarDate(year = thirdNum, month = secondMonth, day = firstNum)
            }

            // Format 2: yyyy-MM-dd (ISO)
            val isoYear = dashParts[0].toIntOrNull()
            val isoMonth = dashParts[1].toIntOrNull()
            val isoDay = dashParts[2].toIntOrNull()
            if (isoYear != null && isoYear > 1900 && isoMonth != null && isoMonth in 1..12 && isoDay != null && isoDay in 1..31) {
                return CalendarDate(year = isoYear, month = isoMonth, day = isoDay)
            }
        }

        // Format 3: dd/MM/yyyy
        val slashParts = text.split("/")
        if (slashParts.size == 3) {
            val p0 = slashParts[0].toIntOrNull()
            val p1 = slashParts[1].toIntOrNull()
            val p2 = slashParts[2].toIntOrNull()
            if (p0 != null && p1 != null && p2 != null) {
                if (p2 > 1900 && p1 in 1..12 && p0 in 1..31) {
                    return CalendarDate(year = p2, month = p1, day = p0)
                }
                if (p0 > 1900 && p1 in 1..12 && p2 in 1..31) {
                    return CalendarDate(year = p0, month = p1, day = p2)
                }
            }
        }
        return null
    }

    private fun parseCsvRows(input: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        var currentRow = mutableListOf<String>()
        val field = StringBuilder()
        var inQuotes = false
        var i = 0
        val n = input.length

        while (i < n) {
            val ch = input[i]
            when {
                inQuotes -> {
                    if (ch == '"') {
                        if (i + 1 < n && input[i + 1] == '"') {
                            field.append('"')
                            i++
                        } else {
                            inQuotes = false
                        }
                    } else {
                        field.append(ch)
                    }
                }
                ch == '"' -> {
                    inQuotes = true
                }
                ch == ',' -> {
                    currentRow.add(field.toString())
                    field.setLength(0)
                }
                ch == '\r' -> {
                    currentRow.add(field.toString())
                    field.setLength(0)
                    rows.add(currentRow)
                    currentRow = mutableListOf()
                    if (i + 1 < n && input[i + 1] == '\n') {
                        i++
                    }
                }
                ch == '\n' -> {
                    currentRow.add(field.toString())
                    field.setLength(0)
                    rows.add(currentRow)
                    currentRow = mutableListOf()
                }
                else -> {
                    field.append(ch)
                }
            }
            i++
        }

        if (field.isNotEmpty() || currentRow.isNotEmpty()) {
            currentRow.add(field.toString())
            rows.add(currentRow)
        }
        return rows
    }
}
