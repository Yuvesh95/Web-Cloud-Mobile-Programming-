package com.simplemobiletools.calendar.helpers

import com.simplemobiletools.calendar.extensions.isXMonthlyRepetition
import com.simplemobiletools.calendar.extensions.isXWeeklyRepetition
import com.simplemobiletools.calendar.extensions.isXYearlyRepetition
import com.simplemobiletools.calendar.extensions.seconds
import com.simplemobiletools.calendar.models.Event
import com.simplemobiletools.calendar.models.RepeatRule
import com.simplemobiletools.commons.helpers.*
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

class Parser {
    // from RRULE:FREQ=DAILY;COUNT=5 to Daily, 5x...
    fun parseRepeatInterval(fullString: String, startTS: Int): RepeatRule {
        val parts = fullString.split(";")
        var repeatInterval = 0
        var repeatRule = 0
        var repeatLimit = 0
        if (fullString.isEmpty()) {
            return RepeatRule(repeatInterval, repeatRule, repeatLimit)
        }

        for (part in parts) {
            val keyValue = part.split("=")
            val key = keyValue[0]
            val value = keyValue[1]
            if (key == FREQ) {
                repeatInterval = getFrequencySeconds(value)
                if (value == WEEKLY) {
                    val start = Formatter.getDateTimeFromTS(startTS)
                    repeatRule = Math.pow(2.0, (start.dayOfWeek - 1).toDouble()).toInt()
                } else if (value == MONTHLY || value == YEARLY) {
                    repeatRule = REPEAT_SAME_DAY
                }
            } else if (key == COUNT) {
                repeatLimit = -value.toInt()
            } else if (key == UNTIL) {
                repeatLimit = parseDateTimeValue(value)
            } else if (key == INTERVAL) {
                repeatInterval *= value.toInt()
            } else if (key == BYDAY) {
                if (repeatInterval.isXWeeklyRepetition()) {
                    repeatRule = handleRepeatRule(value)
                } else if (repeatInterval.isXMonthlyRepetition() || repeatInterval.isXYearlyRepetition()) {
                    repeatRule = if (value.startsWith("-1")) REPEAT_ORDER_WEEKDAY_USE_LAST else REPEAT_ORDER_WEEKDAY
                }
            } else if (key == BYMONTHDAY && value.toInt() == -1) {
                repeatRule = REPEAT_LAST_DAY
            }
        }
        return RepeatRule(repeatInterval, repeatRule, repeatLimit)
    }

    private fun getFrequencySeconds(interval: String) = when (interval) {
        DAILY -> DAY
        WEEKLY -> WEEK
        MONTHLY -> MONTH
        YEARLY -> YEAR
        else -> 0
    }

    private fun handleRepeatRule(value: String): Int {
        var newRepeatRule = 0
        if (value.contains(MO))
            newRepeatRule = newRepeatRule or MONDAY_BIT
        if (value.contains(TU))
            newRepeatRule = newRepeatRule or TUESDAY_BIT
        if (value.contains(WE))
            newRepeatRule = newRepeatRule or WEDNESDAY_BIT
        if (value.contains(TH))
            newRepeatRule = newRepeatRule or THURSDAY_BIT
        if (value.contains(FR))
            newRepeatRule = newRepeatRule or FRIDAY_BIT
        if (value.contains(SA))
            newRepeatRule = newRepeatRule or SATURDAY_BIT
        if (value.contains(SU))
            newRepeatRule = newRepeatRule or SUNDAY_BIT
        return newRepeatRule
    }

    fun parseDateTimeValue(value: String): Int {
        val edited = value.replace("T", "").replace("Z", "")
        return if (edited.length == 14) {
            parseLongFormat(edited, value.endsWith("Z"))
        } else {
            val dateTimeFormat = DateTimeFormat.forPattern("yyyyMMdd")
            dateTimeFormat.parseDateTime(edited).withHourOfDay(5).seconds()
        }
    }

    private fun parseLongFormat(digitString: String, useUTC: Boolean): Int {
        val dateTimeFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss")
        val dateTimeZone = if (useUTC) DateTimeZone.UTC else DateTimeZone.getDefault()
        return dateTimeFormat.parseDateTime(digitString).withZoneRetainFields(dateTimeZone).seconds()
    }

    // from Daily, 5x... to RRULE:FREQ=DAILY;COUNT=5
    fun getRepeatCode(event: Event): String {
        val repeatInterval = event.repeatInterval
        if (repeatInterval == 0)
            return ""

        val freq = getFreq(repeatInterval)
        val interval = getInterval(repeatInterval)
        val repeatLimit = getRepeatLimitString(event)
        val byMonth = getByMonth(event)
        val byDay = getByDay(event)
        return "$FREQ=$freq;$INTERVAL=$interval$repeatLimit$byMonth$byDay"
    }

    private fun getFreq(interval: Int) = when {
        interval % YEAR == 0 -> YEARLY
        interval % MONTH == 0 -> MONTHLY
        interval % WEEK == 0 -> WEEKLY
        else -> DAILY
    }

    private fun getInterval(interval: Int) = when {
        interval % YEAR == 0 -> interval / YEAR
        interval % MONTH == 0 -> interval / MONTH
        interval % WEEK == 0 -> interval / WEEK
        else -> interval / DAY
    }

    private fun getRepeatLimitString(event: Event) = when {
        event.repeatLimit == 0 -> ""
        event.repeatLimit < 0 -> ";$COUNT=${-event.repeatLimit}"
        else -> ";$UNTIL=${Formatter.getDayCodeFromTS(event.repeatLimit)}"
    }

    private fun getByMonth(event: Event) = when {
        event.repeatInterval.isXYearlyRepetition() -> {
            val start = Formatter.getDateTimeFromTS(event.startTS)
            ";$BYMONTH=${start.monthOfYear}"
        }
        else -> ""
    }

    private fun getByDay(event: Event) = when {
        event.repeatInterval.isXWeeklyRepetition() -> {
            val days = getByDayString(event.repeatRule)
            ";$BYDAY=$days"
        }
        event.repeatInterval.isXMonthlyRepetition() || event.repeatInterval.isXYearlyRepetition() -> when (event.repeatRule) {
            REPEAT_LAST_DAY -> ";$BYMONTHDAY=-1"
            REPEAT_ORDER_WEEKDAY_USE_LAST, REPEAT_ORDER_WEEKDAY -> {
                val start = Formatter.getDateTimeFromTS(event.startTS)
                val dayOfMonth = start.dayOfMonth
                val isLastWeekday = start.monthOfYear != start.plusDays(7).monthOfYear
                val order = if (isLastWeekday) "-1" else ((dayOfMonth - 1) / 7 + 1).toString()
                val day = getDayLetters(start.dayOfWeek)
                ";$BYDAY=$order$day"
            }
            else -> ""
        }
        else -> ""
    }

    private fun getByDayString(rule: Int): String {
        var result = ""
        if (rule and MONDAY_BIT != 0)
            result += "$MO,"
        if (rule and TUESDAY_BIT != 0)
            result += "$TU,"
        if (rule and WEDNESDAY_BIT != 0)
            result += "$WE,"
        if (rule and THURSDAY_BIT != 0)
            result += "$TH,"
        if (rule and FRIDAY_BIT != 0)
            result += "$FR,"
        if (rule and SATURDAY_BIT != 0)
            result += "$SA,"
        if (rule and SUNDAY_BIT != 0)
            result += "$SU,"
        return result.trimEnd(',')
    }

    private fun getDayLetters(dayOfWeek: Int) = when (dayOfWeek) {
        1 -> MO
        2 -> TU
        3 -> WE
        4 -> TH
        5 -> FR
        6 -> SA
        else -> SU
    }

    // from P0DT1H5M0S to 3900 (seconds)
    fun parseDurationSeconds(duration: String): Int {
        val weeks = getDurationValue(duration, "W")
        val days = getDurationValue(duration, "D")
        val hours = getDurationValue(duration, "H")
        val minutes = getDurationValue(duration, "M")
        val seconds = getDurationValue(duration, "S")

        val minSecs = 60
        val hourSecs = minSecs * 60
        val daySecs = hourSecs * 24
        val weekSecs = daySecs * 7

        return seconds + (minutes * minSecs) + (hours * hourSecs) + (days * daySecs) + (weeks * weekSecs)
    }

    private fun getDurationValue(duration: String, char: String) = Regex("[0-9]+(?=$char)").find(duration)?.value?.toInt() ?: 0

    // from 65 to P0DT1H5M0S
    fun getDurationCode(minutes: Int): String {
        var days = 0
        var hours = 0
        var remainder = minutes
        if (remainder >= DAY_MINUTES) {
            days = Math.floor((remainder / DAY_MINUTES).toDouble()).toInt()
            remainder -= days * DAY_MINUTES
        }
        if (remainder >= 60) {
            hours = Math.floor((remainder / 60).toDouble()).toInt()
            remainder -= hours * 60
        }
        return "P${days}DT${hours}H${remainder}M0S"
    }
}
