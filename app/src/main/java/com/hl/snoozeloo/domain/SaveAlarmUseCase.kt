package com.hl.snoozeloo.domain

import com.hl.snoozeloo.data.local.AlarmRepository

class SaveAlarmUseCase(
    private val repo: AlarmRepository
) {
    suspend operator fun invoke(alarm: AlarmDetails) = repo.insertAlarm(
        alarm = alarm
    )
}