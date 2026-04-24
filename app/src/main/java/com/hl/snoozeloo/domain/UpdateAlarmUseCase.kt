package com.hl.snoozeloo.domain

import com.hl.snoozeloo.data.local.AlarmRepository

class UpdateAlarmUseCase(
    private val repo: AlarmRepository
) {
    //suspend operator fun invoke(alarm: AlarmDetails) = repo.updateAlarm(alarm)

    /*
    Testing
     */
    var shouldFail = false

    suspend operator fun invoke(alarm: AlarmDetails) {
        if (shouldFail) {
            throw Exception("Database connection lost.")
        } else {
            repo.updateAlarm(alarm)
        }
    }

}

