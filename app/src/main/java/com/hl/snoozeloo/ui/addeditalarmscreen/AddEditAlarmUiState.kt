package com.hl.snoozeloo.ui.addeditalarmscreen

import com.hl.snoozeloo.domain.AlarmDetails

data class AddEditAlarmUiState(
    val alarmDetails: AlarmDetails = AlarmDetails(),
    val hourInput: String = "08",
    val minuteInput: String = "00",
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val isSaveEnabled: Boolean
        get() = hourInput.toIntOrNull() != null && minuteInput.toIntOrNull() != null
}

