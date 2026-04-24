package com.hl.snoozeloo.data.local

import com.hl.snoozeloo.data.mapper.toDomain
import com.hl.snoozeloo.data.mapper.toEntity
import com.hl.snoozeloo.domain.AlarmDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineAlarmRepository(
    private val alarmDao: AlarmDao
) : AlarmRepository {
    override fun getAllAlarmsStream(): Flow<List<AlarmDetails>> {
        return alarmDao.getAllAlarms().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAlarmById(id: Int): AlarmDetails? {
        val entity = alarmDao.getAlarmById(id)
        return entity?.toDomain()
    }

    override suspend fun insertAlarm(alarm: AlarmDetails) {
        alarmDao.addAlarm(alarm.toEntity())
    }

    override suspend fun deleteAlarm(alarm: AlarmDetails) {
        alarmDao.deleteAlarm(alarm.toEntity())
    }

    override suspend fun updateAlarm(alarm: AlarmDetails) {
        alarmDao.updateAlarm(alarm.toEntity())
    }

    override suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }

}