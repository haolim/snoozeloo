package com.hl.snoozeloo

import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenAction
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenViewModel
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalTime

class AddEditAlarmViewModelTest {
    val mockRepository = mockk<AlarmRepository>(relaxed = true)
    val viewModel = AddEditAlarmScreenViewModel(mockRepository)

    @Test
    fun `when onSaveClick is triggered, repository inserts alarm and state updates to success`() = runTest {
        // 1.GIVEN - Set up the initial details in the UI state
        val testDetails = AlarmDetails(
            time = LocalTime.of(11, 50),
            alarmTitle = "Breakfast",
            isEnabled = true
        )
        viewModel.updateUiState(testDetails)

        // 2.WHEN - Trigger the save action
        viewModel.onAction(AddEditAlarmScreenAction.onSaveClick)

        // 3.THEN - Verify the repository was called with the right data
        coVerify { mockRepository.insertAlarm(testDetails) }

        // 4.AND - Verify UI State reflects success
        assertEquals(false, viewModel.uiState.value.isSaving)
        assertEquals(true, viewModel.uiState.value.isSaveSuccess)
    }
}
