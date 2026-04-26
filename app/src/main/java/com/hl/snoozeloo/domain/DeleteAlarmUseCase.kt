package com.hl.snoozeloo.domain

import com.hl.snoozeloo.data.local.AlarmRepository

/*
Temporary UseCase for Testing Purpose Only
 */
class DeleteAlarmUseCase(
    private val repo: AlarmRepository
) {
    suspend operator fun invoke(id: Int) = repo.deleteAlarmById(id)
}