package com.hl.snoozeloo.ui.youralarmscreen

import com.hl.snoozeloo.domain.YourAlarmState

data class YourAlarmUiState(
    val alarms: List<YourAlarmState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
