package com.hl.snoozeloo.data.local

import com.hl.snoozeloo.domain.AlarmDetails
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAllAlarmsStream(): Flow<List<AlarmDetails>>

    suspend fun getAlarmById(id: Int): AlarmDetails?

    suspend fun insertAlarm(alarm: AlarmDetails)

    suspend fun deleteAlarm(alarm: AlarmDetails)

    suspend fun deleteAlarmById(id: Int)

    /*
    For testing purpose only. To be deleted.
     */
//    val alarmDao: AlarmDao
//    suspend fun clearAllAlarms() {
//        alarmDao.deleteAllAlarms()
//    }

}