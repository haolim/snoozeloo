package com.hl.snoozeloo.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hl.snoozeloo.AlarmApplication
import com.hl.snoozeloo.domain.DeleteAlarmUseCase
import com.hl.snoozeloo.domain.GetAlarmByIdUseCase
import com.hl.snoozeloo.domain.GetAllAlarmsUseCase
import com.hl.snoozeloo.domain.SaveAlarmUseCase
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenViewModel
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmsScreenViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val repo = alarmApplication().container.alarmRepository
            val getAllAlarms = GetAllAlarmsUseCase(repo)
            // Temporary UseCase for Testing Purpose. To delete.
            val deleteAllAlarms = DeleteAlarmUseCase(repo)
            // ------------------------------------------------------------
            val insertOrUpdateAlarm = SaveAlarmUseCase(repo)
            YourAlarmsScreenViewModel(
                getAllAlarmsUseCase = getAllAlarms,
                deleteAlarmUseCase = deleteAllAlarms, // Temporary UseCase for Testing Purpose. To delete.
                saveAlarmUseCase = insertOrUpdateAlarm
            )
        }
        initializer {
            val repo = alarmApplication().container.alarmRepository
            val saveAlarmUseCase = SaveAlarmUseCase(repo)
            val getAlarmByIdUseCase = GetAlarmByIdUseCase(repo)
            val savedStateHandle = createSavedStateHandle()

            AddEditAlarmScreenViewModel(
                saveAlarmUseCase = saveAlarmUseCase,
                getAlarmByIdUseCase = getAlarmByIdUseCase,
                savedStateHandle = savedStateHandle,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [AlarmApplication].
 */

fun CreationExtras.alarmApplication() : AlarmApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AlarmApplication)