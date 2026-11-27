package com.example.data

object OdiaFormatting {

    private val odiaDigits = charArrayOf('୦', '୧', '୨', '୩', '୪', '୫', '୬', '୭', '୮', '୯')

    val weekdayHeadersOdia = listOf("ରବି", "ସୋମ", "ମଙ୍ଗଳ", "ବୁଧ", "ଗୁରୁ", "ଶୁକ୍ର", "ଶନି")

    private val fullWeekdaysOdia = listOf(
        "ରବିବାର",
        "ସୋମବାର",
        "ମଙ୍ଗଳବାର",
        "ବୁଧବାର",
        "ଗୁରୁବାର",
        "ଶୁକ୍ରବାର",
        "ଶନିବାର"
    )

    val monthsOdia = listOf(
        "ଜାନୁଆରୀ",
        "ଫେବୃଆରୀ",
        "ମାର୍ଚ୍ଚ",
        "ଅପ୍ରେଲ",
        "ମଇ",
        "ଜୁନ",
        "ଜୁଲାଇ",
        "ଅଗଷ୍ଟ",
        "ସେପ୍ଟେମ୍ବର",
        "ଅକ୍ଟୋବର",
        "ନଭେମ୍ବର",
        "ଡିସେମ୍ବର"
    )

    val monthsEnglish = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    /**
     * Traditional Odia solar month spans (e.g. ଧନୁ ୧୭ ଠାରୁ ମକର ୧୮ ପର୍ଯ୍ୟନ୍ତ) for 2027.
     */
    private val odiaSolarSpan2027 = listOf(
        "ଧନୁ ୧୭ ଠାରୁ ମକର ୧୮ ପର୍ଯ୍ୟନ୍ତ",
        "ମକର ୧୯ ଠାରୁ କୁମ୍ଭ ୧୬ ପର୍ଯ୍ୟନ୍ତ",
        "କୁମ୍ଭ ୧୭ ଠାରୁ ମୀନ ୧୭ ପର୍ଯ୍ୟନ୍ତ",
        "ମୀନ ୧୮ ଠାରୁ ମେଷ ୧୭ ପର୍ଯ୍ୟନ୍ତ",
        "ମେଷ ୧୮ ଠାରୁ ବୃଷ ୧୭ ପର୍ଯ୍ୟନ୍ତ",
        "ବୃଷ ୧୮ ଠାରୁ ମିଥୁନ ୧୬ ପର୍ଯ୍ୟନ୍ତ",
        "ମିଥୁନ ୧୭ ଠାରୁ କର୍କଟ ୧୫ ପର୍ଯ୍ୟନ୍ତ",
        "କର୍କଟ ୧୬ ଠାରୁ ସିଂହ ୧୫ ପର୍ଯ୍ୟନ୍ତ",
        "ସିଂହ ୧୬ ଠାରୁ କନ୍ୟା ୧୪ ପର୍ଯ୍ୟନ୍ତ",
        "କନ୍ୟା ୧୫ ଠାରୁ ତୁଳା ୧୪ ପର୍ଯ୍ୟନ୍ତ",
        "ତୁଳା ୧୫ ଠାରୁ ବିଛା ୧୪ ପର୍ଯ୍ୟନ୍ତ",
        "ବିଛା ୧୫ ଠାରୁ ଧନୁ ୧୬ ପର୍ଯ୍ୟନ୍ତ"
    )

    fun toOdiaDigits(number: Int): String = toOdiaDigits(number.toString())

    fun toOdiaDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(odiaDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun monthNameOdia(month: Int): String =
        monthsOdia.getOrElse(month - 1) { "" }

    fun monthNameEnglish(month: Int): String =
        monthsEnglish.getOrElse(month - 1) { "" }

    fun odiaSolarSpan(month: Int): String =
        odiaSolarSpan2027.getOrElse(month - 1) { "" }

    /**
     * Returns traditional Sakabda and Samvat line for the header banner.
     */
    fun sakabdaSamvatLine(year: Int, month: Int): String {
        val sakabda = if (month < 4) year - 79 else year - 78
        val samvat = if (month < 4) year + 56 else year + 57
        return "ଶକାବ୍ଦ ${toOdiaDigits(sakabda)} • ସମ୍ବତ ${toOdiaDigits(samvat)}"
    }

    fun fullWeekdayOdia(dayOfWeek: Int): String =
        fullWeekdaysOdia.getOrElse(dayOfWeek - 1) { "" }

    fun formatDateOdiaFull(date: CalendarDate): String {
        return "${toOdiaDigits(date.day)} ${monthNameOdia(date.month)} ${toOdiaDigits(date.year)}"
    }

    fun formatDateEnglishFull(date: CalendarDate): String {
        return "${date.day} ${monthNameEnglish(date.month)} ${date.year}"
    }

    fun formatTime12HourOdia(hour24: Int, minute: Int): String {
        val period = if (hour24 < 12) "AM" else "PM"
        val hour12 = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        return toOdiaDigits("%02d:%02d %s".format(hour12, minute, period))
    }
}
