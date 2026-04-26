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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAlarm(alarm: AlarmEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): AlarmEntity?

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarmById(id: Int)

    /*
    Because of the Flow return type, Room also runs the query on the background thread.
    You don't need to explicitly make it a suspend function and call it inside a coroutine scope.
     */

    @Query("SELECT * FROM alarms ORDER BY id ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>


}