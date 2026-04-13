package com.hl.snoozeloo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val alarmTitle: String,
    val isEnabled: Boolean,
)
