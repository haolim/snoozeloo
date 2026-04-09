package com.hl.snoozeloo.ui.youralarmscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hl.snoozeloo.domain.YourAlarmState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class YourAlarmsScreenViewModel: ViewModel() {
    //private val _uiState = MutableStateFlow(YourAlarmUiState()
//        (
//        isLoading = false)
 //   )
    //val uiState: StateFlow<YourAlarmUiState> = _uiState.asStateFlow()

    // Keep the time updated every 60 seconds
    val currentTimeFlow = flow {
        while(true) {
            emit(LocalTime.now())
            // Wait exactly until the next clock changes to update the UI
            val second = LocalTime.now().second
            val millisUntilNextMinute = (60 - second) * 1_000L
            delay(millisUntilNextMinute)
        }
    }

    private val _alarms = MutableStateFlow<List<YourAlarmState>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    val uiState: StateFlow<YourAlarmUiState> = combine(
        _alarms,
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = YourAlarmUiState(isLoading = true)
    )

    init {
        loadAlarms()
    }

    fun onAction(action: YourAlarmsScreenAction) {
        when(action) {
            is YourAlarmsScreenAction.turnOnOrOffAlarm -> {

            }
        }
    }

    private fun loadAlarms() {
//        viewModelScope.launch {
//            delay(2000)
//            //        val savedAlarms = repository.getAllAlarms()
//        _uiState.update { it.copy(
//            //        alarms = savedAlarms,
//            alarms = listOf(
//                YourAlarmState(
//                    time = LocalTime.of(10, 0),
//                    alarmTitle = "Wake Up",
//                    isEnabled = true,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(10, 0))
//                    ),
//                YourAlarmState(
//                    time = LocalTime.of(16, 0),
//                    alarmTitle = "Education",
//                    isEnabled = true,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(16,0))
//                ),
//                YourAlarmState(
//                    time = LocalTime.of(18, 0),
//                    alarmTitle = "Dinner",
//                    isEnabled = false,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(18,0))
//                ),
//            ),
//                isLoading = false
//                    )}
//        }

        viewModelScope.launch {
            delay(2000)

            _alarms.value = listOf(
                YourAlarmState(
                    time = LocalTime.of(10, 0),
                    alarmTitle = "Wake Up",
                    isEnabled = true,
                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(10, 0))
                    ),
                YourAlarmState(
                    time = LocalTime.of(16, 0),
                    alarmTitle = "Education",
                    isEnabled = true,
                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(16,0))
                ),
                YourAlarmState(
                    time = LocalTime.of(18, 0),
                    alarmTitle = "Dinner",
                    isEnabled = false,
                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(18,0))
                ),
                YourAlarmState(
                    time = LocalTime.of(6, 0),
                    alarmTitle = "Dinner",
                    isEnabled = false,
                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(6,0))
            )
            )
                _isLoading.value = false
        }
    }
}