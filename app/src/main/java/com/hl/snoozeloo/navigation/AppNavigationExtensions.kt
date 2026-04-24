package com.hl.snoozeloo.navigation

import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenRoot
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmScreen
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmScreenRoot

/*
Implement Type-Safe Navigation
TODO: Migrate to Navigation 3
 */
fun NavGraphBuilder.homeGraph(
    onNavigateToAdd: () -> Unit,
    onAlarmEditClicked: (Int) -> Unit
) {
    composable<Route.Home> {
        YourAlarmScreenRoot(
            onAddAlarmClicked = onNavigateToAdd,
            onEditAlarmClicked = onAlarmEditClicked,
        )
    }
}

fun NavGraphBuilder.addEditAlarmGraph(
    onBack: () -> Unit,
) {
    composable<Route.AddEditAlarm> {
        AddEditAlarmScreenRoot(
            onBackClicked = onBack
        )
    }

}