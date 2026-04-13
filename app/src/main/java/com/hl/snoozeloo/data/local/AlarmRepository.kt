package com.hl.snoozeloo.data.local

import com.hl.snoozeloo.domain.AlarmDetails
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAllAlarmsStream(): Flow<List<AlarmDetails>>

    fun getAlarmStream(id: Int): Flow<AlarmDetails?>

    suspend fun insertAlarm(alarm: AlarmDetails)

    suspend fun deleteAlarm(alarm: AlarmDetails)

    suspend fun updateAlarm(alarm: AlarmDetails)

    /*
    For testing purpose only. To be deleted.
     */
    val alarmDao: AlarmDao
    suspend fun clearAllAlarms() {
        alarmDao.deleteAllAlarms()
    }

}