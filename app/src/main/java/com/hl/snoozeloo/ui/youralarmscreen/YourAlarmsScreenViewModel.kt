package com.hl.snoozeloo.ui.youralarmscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.GetAllAlarmsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime

class YourAlarmsScreenViewModel(
    private val alarmRepository: AlarmRepository, // temporary for testing only. To be removed.
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase
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

    private val _alarms = MutableStateFlow<List<AlarmDetails>>(emptyList()) // We can remove this and move it into a UseCase
    private val _isLoading = MutableStateFlow(true)


    init {
        insertData() // Temporary for testing only. To be removed.
        //loadAlarms()
    }

    fun onAction(action: YourAlarmsScreenAction) {
        when(action) {
            is YourAlarmsScreenAction.turnOnOrOffAlarm -> {

            }
        }
    }
    /*
    Temporary data for Testing Only. To be removed
     */
    // --------------------------------------------------- //
    private val _alarmFlows = alarmRepository.getAllAlarmsStream()
    private fun insertData() {
        viewModelScope.launch {
            delay(10_000)
            alarmRepository.insertAlarm(
                alarm = AlarmDetails(
                    time = LocalTime.of(10, 50),
                    alarmTitle = "Breakfast",
                    isEnabled = true
                )
            )
            alarmRepository.insertAlarm(
                alarm = AlarmDetails(
                    time = LocalTime.of(16, 0),
                    alarmTitle = "Education",
                    isEnabled = true
                )
            )
            alarmRepository.insertAlarm(
                alarm = AlarmDetails(
                    time = LocalTime.of(19, 30),
                    alarmTitle = "Dinner",
                    isEnabled = true
                )
            )
        }
    }
    // --------------------------------------------------- //

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

//            _alarms.value = listOf(
//                AlarmDetails(
//                    time = LocalTime.of(10, 0),
//                    alarmTitle = "Wake Up",
//                    isEnabled = true,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(10, 0))
//                    ),
//                AlarmDetails(
//                    time = LocalTime.of(16, 0),
//                    alarmTitle = "Education",
//                    isEnabled = true,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(16,0))
//                ),
//                AlarmDetails(
//                    time = LocalTime.of(18, 0),
//                    alarmTitle = "Dinner",
//                    isEnabled = false,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(18,0))
//                ),
//                AlarmDetails(
//                    time = LocalTime.of(6, 0),
//                    alarmTitle = "Dinner",
//                    isEnabled = false,
//                    timeLeftDescription = LocalTime.now().timeLeftUntil(LocalTime.of(6,0))
//            )
//            )
                _isLoading.value = false
        }
    }

    val uiState: StateFlow<YourAlarmUiState> = combine(
        //getAllAlarmsUseCase(),
        _alarmFlows,
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
}