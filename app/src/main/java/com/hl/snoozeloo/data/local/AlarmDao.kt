package com.hl.snoozeloo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    /*
     Insert/Add new item and since we only add it from one place, the AddEditAlarmScreen,
     we can use IGNORE strategy as we are not expecting any conflicts
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarm: AlarmEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlarm(alarm: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)

    /*
    Because of the Flow return type, Room also runs the query on the background thread.
    You don't need to explicitly make it a suspend function and call it inside a coroutine scope.
     */
    @Query("SELECT * FROM alarms where id = :id")
    fun getAlarmById(id: Int): Flow<AlarmEntity>

    @Query("SELECT * FROM alarms ORDER BY id ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    /*
    Temporary for testing only. To be deleted.
     */
    @Query("DELETE FROM alarms")
    suspend fun deleteAllAlarms()
}