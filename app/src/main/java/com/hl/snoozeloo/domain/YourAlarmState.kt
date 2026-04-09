package com.hl.snoozeloo.domain

import java.time.LocalTime

data class YourAlarmState(
    val time: LocalTime = LocalTime.of(8, 0),
    val alarmTitle: String = "",
    val isEnabled: Boolean = false,
    val timeLeftDescription: String = "",
)

