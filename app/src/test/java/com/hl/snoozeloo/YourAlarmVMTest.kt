package com.hl.snoozeloo

import app.cash.turbine.test
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.domain.DeleteAllAlarmsUseCase
import com.hl.snoozeloo.domain.GetAllAlarmsUseCase
import com.hl.snoozeloo.domain.UpdateAlarmUseCase
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
    val updateAlarmUseCase = mockk<UpdateAlarmUseCase>(relaxed = true)
    val getAllAlarmsUseCase = mockk<GetAllAlarmsUseCase>(relaxed = true)

    //Temp
    val deleteAllAlarmsUseCase = mockk<DeleteAllAlarmsUseCase>(relaxed = true)

    val viewModel = YourAlarmsScreenViewModel(getAllAlarmsUseCase = getAllAlarmsUseCase,
        updateAlarmUseCase = updateAlarmUseCase,
        deleteAllAlarmsUseCase = deleteAllAlarmsUseCase
    )

    @Test
    fun `when update fails, snackbar event is sent`() = runTest {
        // Arrange: Force the use case to throw an exception
        coEvery { updateAlarmUseCase(any()) } throws Exception("Mock failure")

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