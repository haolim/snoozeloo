package com.hl.snoozeloo.ui.addeditalarmscreen

sealed interface AddEditAlarmScreenAction {

    data object onSaveClick: AddEditAlarmScreenAction

    data class onHourChange(val newHour: String): AddEditAlarmScreenAction

    data class onMinuteChange(val newMinute: String): AddEditAlarmScreenAction

}