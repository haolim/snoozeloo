package com.hl.snoozeloo.domain

import com.hl.snoozeloo.data.local.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmByIdUseCase(
    private val repo: AlarmRepository
) {
    suspend operator fun invoke(alarmId: Int): AlarmDetails? {
        return repo.getAlarmById(alarmId)
    }
}