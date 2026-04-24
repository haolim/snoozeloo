package com.hl.snoozeloo.domain

import java.time.LocalTime

data class AlarmDetails(
    val id: Int = 0,
    val time: LocalTime = LocalTime.of(8, 0),
    val alarmTitle: String = "",
    val isEnabled: Boolean = true,
    val timeLeftDescription: String = "",
)

