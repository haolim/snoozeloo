package com.hl.snoozeloo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hl.snoozeloo.navigation.Route

/*
Implement Type-Safe Navigation
TODO: Migrate to Navigation 3
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home
    ) {
        homeGraph(
            onNavigateToAdd = {
                navController.navigate(Route.AddEditAlarm(alarmId = null))
            },
            onAlarmEditClicked = { id ->
                navController.navigate(Route.AddEditAlarm(alarmId = id))
            }
        )

        addEditAlarmGraph(
            onBack = {
                navController.popBackStack()
            }
        )
    }
}