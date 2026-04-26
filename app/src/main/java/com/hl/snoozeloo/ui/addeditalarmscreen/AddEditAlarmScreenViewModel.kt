package com.hl.snoozeloo.ui.addeditalarmscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.GetAlarmByIdUseCase
import com.hl.snoozeloo.domain.SaveAlarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime


class AddEditAlarmScreenViewModel(
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val alarmId: Int? = savedStateHandle.get<Int>("alarmId")
    private val _uiState = MutableStateFlow(AddEditAlarmUiState())
    val uiState: StateFlow<AddEditAlarmUiState> = _uiState.asStateFlow()

    init {
        alarmId?.let { id ->
            loadExistingAlarm(id)
        }
    }
    private fun loadExistingAlarm(id: Int) {
        viewModelScope.launch {
            val alarm = getAlarmByIdUseCase(id)
            alarm?.let { alarmDetails ->
                _uiState.update { it.copy(
                    hourInput = alarmDetails.time.hour.toString().padStart(2, '0'),
                    minuteInput = alarmDetails.time.minute.toString().padStart(2, '0'),
                    alarmDetails = alarmDetails
                )}
            }
        }
    }
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
                updateTimeInput(newHour = action.newHour)
            }
            is AddEditAlarmScreenAction.onMinuteChange -> {
                updateTimeInput(newMinute = action.newMinute)
            }
            is AddEditAlarmScreenAction.onSaveClick -> {
                viewModelScope.launch {
                    saveAlarm()
                }
            }
        }
    }

    private fun updateTimeInput(newHour: String? = null, newMinute: String? = null) {
        _uiState.update { currentState ->
            // 1. Determine which value is changing and sanitize it
            val rawInput = newHour ?: newMinute ?: ""
            val cleanInput = rawInput.filter { it.isDigit() }.takeLast(2)
            val inputInt = cleanInput.toIntOrNull() ?: 0

            // 2. Validate ranges based on what is being updated
            val isHourValid = newHour == null || inputInt in 0..23
            val isMinuteValid = newMinute == null || inputInt in 0..59

            if (!isHourValid || !isMinuteValid) {
                return@update currentState // Ignore invalid input
            }

            // 3. Prepare the new strings for the UI
            val nextHour = if (newHour != null) cleanInput else currentState.hourInput
            val nextMinute = if (newMinute != null) cleanInput else currentState.minuteInput

            // 4. Update the domain model (LocalTime)
            // We use 0 as fallback for empty strings so LocalTime.of doesn't crash
            val updatedLocalTime = try {
                LocalTime.of(
                    nextHour.ifEmpty { "0" }.toInt(),
                    nextMinute.ifEmpty { "0" }.toInt()
                )
            } catch (e: Exception) {
                currentState.alarmDetails.time
            }

            currentState.copy(
                hourInput = nextHour,
                minuteInput = nextMinute,
                alarmDetails = currentState.alarmDetails.copy(
                    time = updatedLocalTime,
                    alarmTitle = currentState.alarmDetails.alarmTitle
                )
            )
        }
    }

    private suspend fun saveAlarm() {
        // Pull AlarmDetails from Domain Model out of the UI State
        // and send it to the Repository
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        try {
            val alarmToSave = _uiState.value.alarmDetails
            saveAlarmUseCase(alarmToSave)
            _uiState.update { it.copy(isSaveSuccess = true)}
        } catch (e: Exception) {
            _uiState.update { it.copy(isSaving = false, errorMessage = "Error saving alarm. Try again.")}
        }
    }
}