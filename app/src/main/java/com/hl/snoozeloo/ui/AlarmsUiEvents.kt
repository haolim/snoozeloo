package com.hl.snoozeloo.ui

sealed interface AlarmsUiEvents {
    data class ShowSnackBar(val message: String): AlarmsUiEvents
}