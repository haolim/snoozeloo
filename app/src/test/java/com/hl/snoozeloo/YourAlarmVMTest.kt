package com.hl.snoozeloo

import app.cash.turbine.test
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.DeleteAlarmUseCase
import com.hl.snoozeloo.domain.GetAllAlarmsUseCase
import com.hl.snoozeloo.domain.SaveAlarmUseCase
import com.hl.snoozeloo.ui.AlarmsUiEvents
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmsScreenAction
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmsScreenViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

class YourAlarmVMTest {
    val mockAlarm = AlarmDetails(
        id = 1,
        time = LocalTime.of(11, 50),
        alarmTitle = "Breakfast",
        isEnabled = true
    )
    val saveAlarmUseCase = mockk<SaveAlarmUseCase>(relaxed = true)
    val getAllAlarmsUseCase = mockk<GetAllAlarmsUseCase>(relaxed = true)

    //Temp
    val deleteAlarmUseCase = mockk<DeleteAlarmUseCase>(relaxed = true)

    val viewModel = YourAlarmsScreenViewModel(getAllAlarmsUseCase = getAllAlarmsUseCase,
        saveAlarmUseCase = saveAlarmUseCase,
        deleteAlarmUseCase = deleteAlarmUseCase
    )

    @Test
    fun `when update fails, snackbar event is sent`() = runTest {
        // Arrange: Force the use case to throw an exception
        coEvery { saveAlarmUseCase(any()) } throws Exception("Mock failure")

        // Act
        viewModel.onAction(YourAlarmsScreenAction.turnOnOrOffAlarm(mockAlarm))

        // Assert: Collect the channel flow and verify the event
        viewModel.eventFlow.test {
            val event = awaitItem()
            assert(event is AlarmsUiEvents.ShowSnackBar)
            assert(
                (event as AlarmsUiEvents.ShowSnackBar).message == "Error updating alarm. Try again."
            )
        }
    }
}