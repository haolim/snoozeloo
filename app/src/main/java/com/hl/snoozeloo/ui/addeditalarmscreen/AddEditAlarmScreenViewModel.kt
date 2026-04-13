package com.hl.snoozeloo.ui.addeditalarmscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.SaveAlarmUseCase
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime


class AddEditAlarmScreenViewModel(
    //private val alarmRepository: AlarmRepository // To delete
    private val saveAlarmUseCase: SaveAlarmUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AddEditAlarmUiState())
    val uiState: StateFlow<AddEditAlarmUiState> = _uiState.asStateFlow()

    fun updateUiState(newDetails: AlarmDetails) {
        _uiState.update {
            it.copy(
                alarmDetails = newDetails
            )
        }
    }

    fun onAction(action: AddEditAlarmScreenAction) {
        when (action) {
            is AddEditAlarmScreenAction.onHourChange -> {
                updateTime(newHour = action.newHour)
            }
            is AddEditAlarmScreenAction.onMinuteChange -> {
                updateTime(newMinute = action.newMinute)
            }
            is AddEditAlarmScreenAction.onSaveClick -> {
                viewModelScope.launch {
                    saveAlarm()
                }
            }
        }
    }

    private fun updateTime(
        newHour: String? = null,
        newMinute: String? = null
    ) {
        _uiState.update { currentState ->
            val h = newHour ?: currentState.hourInput
            val m = newMinute ?: currentState.minuteInput

            // Try to sync LocalTime for the domain model
            val updatedLocalTime = try {
                LocalTime.of(h.toInt(), m.toInt())
            } catch(e: Exception) {
                currentState.alarmDetails.time // Keep old time if input is invalid/empty
            }
            currentState.copy(
                hourInput = h,
                minuteInput = m,
                alarmDetails = currentState.alarmDetails.copy(time = updatedLocalTime),
            )
        }
    }



    private suspend fun saveAlarm() {
        // Pull AlarmDetails from Domain Model out of the UI State
        // and send it to the Repository

        _uiState.update { it.copy(isSaving = true) }
        try {
            val alarmToSave = _uiState.value.alarmDetails
            saveAlarmUseCase(alarmToSave)
            _uiState.update { it.copy(isSaving = false, isSaveSuccess = true)}
        } catch (e: Exception) {
            _uiState.update { it.copy(isSaving = false, errorMessage = e.message)}
        }
    }
}