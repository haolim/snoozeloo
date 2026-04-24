package com.hl.snoozeloo.ui.youralarmscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.DeleteAllAlarmsUseCase
import com.hl.snoozeloo.domain.GetAllAlarmsUseCase
import com.hl.snoozeloo.domain.UpdateAlarmUseCase
import com.hl.snoozeloo.ui.AlarmsUiEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime

class YourAlarmsScreenViewModel(
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val deleteAllAlarmsUseCase: DeleteAllAlarmsUseCase, // Temporary for testing only. To delete.
    private val updateAlarmUseCase: UpdateAlarmUseCase
) : ViewModel() {

    // Keep the time updated every 60 seconds
    val currentTimeFlow = flow {
        while(true) {
            // Wait exactly until the next clock changes to update the UI
            val now = LocalTime.now()
            emit(now)
            val millisUntilNextMinute = (60 - now.second) * 1_000L
            delay(millisUntilNextMinute + 10) // small buffer in case of clock drift
        }
    }
    private val _isLoading = MutableStateFlow(true)

    // Channel - 1. The Channel (Pipe that sends messages)
    private val _eventChannel = Channel<AlarmsUiEvents>()

    // Channel - 2. The Flow (The 'stream' that the UI observes)
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        loadAlarms()
        clearDatabase()
    }
    val uiState: StateFlow<YourAlarmUiState> = combine(
        getAllAlarmsUseCase(),
        _isLoading,
        currentTimeFlow
    ) { alarms, loading, now ->
        YourAlarmUiState(
            alarms = alarms.map { alarm ->
                alarm.copy(timeLeftDescription = now.timeLeftUntil(alarm.time))
            },
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        // provide similar safety as repeatOnLifecycle but at data stream level. This will stop
        // collecting from currentTimeFlow and _alarms when UI is no longer visible
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = YourAlarmUiState(isLoading = true)
    )

    // Temporary for testing only. To delete.
    private fun clearDatabase() {
        deleteAllAlarmsUseCase
    }
    // --------------------------------------------
    fun onAction(action: YourAlarmsScreenAction) {
        when(action) {
            is YourAlarmsScreenAction.turnOnOrOffAlarm -> {
                val currentAlarm = action.alarm
                val newAlarm = !currentAlarm.isEnabled
                viewModelScope.launch {
                    try {
                        updateAlarmUseCase(
                            alarm = currentAlarm.copy(isEnabled = newAlarm)
                        )
                    } catch (e: Exception) {
                        uiState.value.copy(errorMessage = "Error updating alarm. Try again.")
                        // Channel - 3. Send the error event into the pipe
                        _eventChannel.send(AlarmsUiEvents.ShowSnackBar(uiState.value.errorMessage ?: "Error updating alarm. Try again."))
                    }
                }
            }
            is YourAlarmsScreenAction.onAlarmClicked -> {

            }
            is YourAlarmsScreenAction.addAlarmClicked -> {

            }
        }
    }



    private fun loadAlarms() {
        viewModelScope.launch {
            delay(2000)
                _isLoading.value = false
        }
    }

}