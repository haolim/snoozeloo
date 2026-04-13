package com.hl.snoozeloo.domain

import com.hl.snoozeloo.data.local.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAllAlarmsUseCase(
    private val repo: AlarmRepository
) {
    // invoke: treat this class as a function call
    operator fun invoke(): Flow<List<AlarmDetails>> = repo.getAllAlarmsStream()
}