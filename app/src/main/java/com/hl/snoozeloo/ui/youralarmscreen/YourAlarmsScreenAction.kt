package com.hl.snoozeloo.ui.youralarmscreen

sealed interface YourAlarmsScreenAction {
    data class turnOnOrOffAlarm(val isOn: Boolean): YourAlarmsScreenAction
}