package com.hl.snoozeloo.ui.youralarmscreen

import com.hl.snoozeloo.domain.AlarmDetails

sealed interface YourAlarmsScreenAction {
    data class turnOnOrOffAlarm(val alarm: AlarmDetails): YourAlarmsScreenAction

    data class onAlarmClicked(val id: Int): YourAlarmsScreenAction

    data object addAlarmClicked: YourAlarmsScreenAction
}