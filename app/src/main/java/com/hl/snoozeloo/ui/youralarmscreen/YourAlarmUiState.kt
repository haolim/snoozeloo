package com.hl.snoozeloo.ui.youralarmscreen

import com.hl.snoozeloo.domain.AlarmDetails

data class YourAlarmUiState(
    val alarms: List<AlarmDetails> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
