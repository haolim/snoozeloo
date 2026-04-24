package com.hl.snoozeloo.navigation

import kotlinx.serialization.Serializable

/*
Implement Type-Safe Navigation
TODO: Migrate to Navigation 3
 */
@Serializable
sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data class AddEditAlarm(val alarmId: Int? = null) : Route
}