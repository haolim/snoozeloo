package com.hl.snoozeloo.data.mapper

import com.hl.snoozeloo.data.local.AlarmEntity
import com.hl.snoozeloo.domain.AlarmDetails
import java.time.LocalTime


fun AlarmEntity.toDomain(): AlarmDetails {
    return AlarmDetails(
        id = this.id,
        time = LocalTime.of(this.hour, this.minute),
        alarmTitle = this.alarmTitle,
        isEnabled = this.isEnabled,
    )
}

fun AlarmDetails.toEntity(): AlarmEntity {
    return AlarmEntity(
        id = this.id,
        hour = this.time.hour,
        minute = this.time.minute,
        alarmTitle = this.alarmTitle,
        isEnabled = this.isEnabled
    )
}