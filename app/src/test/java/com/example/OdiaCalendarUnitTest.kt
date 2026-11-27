package com.example

import com.example.data.CalendarDate
import com.example.data.CsvCalendarParser
import com.example.data.OdiaFormatting
import com.example.data.SolarCalculator
import com.example.ui.theme.OdiaTextSizeOption
import com.example.ui.theme.OdiaThemePalette
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class OdiaCalendarUnitTest {

    @Test
    fun bundled2027SheetParsesAll365DaysWithHolidayAndFestivalFlags() {
        val csvFile = File("src/main/assets/odia_calendar_2027.csv")
        assertTrue("Bundled CSV must exist at ${csvFile.absolutePath}", csvFile.exists())

        val entries = CsvCalendarParser.parse(csvFile.readText(Charsets.UTF_8))
        assertEquals(365, entries.size)

        // Makar Sankranti (14-Jan-2027): Full Maroon Holiday with White text
        val makar = entries.first { it.date == CalendarDate(2027, 1, 14) }
        assertTrue(makar.isFullMaroonHoliday)
        assertFalse(makar.isFullSaffronFestival)
        assertEquals("ଷଷ୍ଠୀ", makar.tithi)
        assertEquals("ଉତ୍ତରଭାଦ୍ରପଦ", makar.nakshatra)
        assertEquals("ମୀନ", makar.rashi)
        assertTrue(makar.festival.contains("ମକର ସଂକ୍ରାନ୍ତି"))
        assertFalse(makar.hasShubhaDin)

        // Bakula Amavasya (07-Jan-2027): Full Saffron Festival
        val bakula = entries.first { it.date == CalendarDate(2027, 1, 7) }
        assertFalse(bakula.isFullMaroonHoliday)
        assertTrue(bakula.isFullSaffronFestival)
        assertEquals("ବକୁଳ ଅମାବାସ୍ୟା", bakula.festival)

        // Quoted CSV value with embedded comma: Akshaya Tritiya (09-May-2027)
        val akshaya = entries.first { it.date == CalendarDate(2027, 5, 9) }
        assertTrue(akshaya.isSunday)
        assertTrue(akshaya.isFullSaffronFestival)
        assertEquals("ଅକ୍ଷୟ ତୃତୀୟା, ଚନ୍ଦନ ଯାତ୍ରା ଆରମ୍ଭ ଓ ପରଶୁରାମ ଜୟନ୍ତୀ", akshaya.festival)
    }

    @Test
    fun odiaFormattingAndSolarCalculationsWorkProperly() {
        assertEquals("୨୦୨୭", OdiaFormatting.toOdiaDigits(2027))
        assertEquals("ଶକାବ୍ଦ ୧୯୪୮ • ସମ୍ବତ ୨୦୮୩", OdiaFormatting.sakabdaSamvatLine(2027, 1))
        assertEquals("୦୭:୦୦ AM", OdiaFormatting.formatTime12HourOdia(7, 0))
        assertEquals("୦୬:୩୦ PM", OdiaFormatting.formatTime12HourOdia(18, 30))

        val winterSolar = SolarCalculator.calculateForDate(CalendarDate(2027, 1, 14))
        assertTrue(winterSolar.sunriseEn.endsWith("AM"))
        assertTrue(winterSolar.sunsetEn.endsWith("PM"))
        assertTrue(winterSolar.sunriseOdia.startsWith("୦୬:"))
    }

    @Test
    fun appearanceSettingsOptionsResolveSafely() {
        assertEquals(OdiaTextSizeOption.NORMAL, OdiaTextSizeOption.fromKey(null))
        assertEquals(OdiaTextSizeOption.LARGE, OdiaTextSizeOption.fromKey("LARGE"))
        assertEquals(OdiaTextSizeOption.EXTRA_LARGE, OdiaTextSizeOption.fromKey("extra_large"))
        assertEquals(OdiaThemePalette.VERMILION_HERITAGE, OdiaThemePalette.fromKey("unknown"))
        assertEquals(OdiaThemePalette.TEMPLE_TEAL, OdiaThemePalette.fromKey("TEMPLE_TEAL"))
        assertTrue(OdiaTextSizeOption.EXTRA_LARGE.fontScaleMultiplier > OdiaTextSizeOption.LARGE.fontScaleMultiplier)
    }
}
