package com.hl.snoozeloo.ui.youralarmscreen

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toUserFriendlyStringAMPM(): String {
    val formatter = DateTimeFormatter.ofPattern("a") // e.g., "08:30 AM"
    return this.format(formatter)
}
fun LocalTime.toUserFriendlyStringHHMM(): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm") // e.g., "08:30 AM"
    return this.format(formatter)
}

fun LocalTime.timeLeftUntil(alarmTime: LocalTime): String {
//    val duration = java.time.Duration.between(this, alarmTime)
//
//    val days = duration.toDays()
//    val hours = duration.toHours() % 24
//    val minutes = duration.toMinutes() % 60

    var secondsDiff = java.time.Duration.between(this, alarmTime).seconds

    if (secondsDiff < 0) {
        secondsDiff += 24 * 60 * 60
    }

    val totalMinutes = (secondsDiff + 59) / 60

    val days = totalMinutes / (24 * 60)
    val hours = (totalMinutes % (24 * 60)) / 60
    val minutes = totalMinutes % 60

    return when {
        days > 0 -> "Alarm in ${days}d ${hours}h ${minutes}min"
        hours > 0 -> "Alarm in ${hours}h ${minutes}min"
        else -> "Alarm in ${minutes}min"
    }
}