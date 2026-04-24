package com.hl.snoozeloo.ui.youralarmscreen


import android.R.attr.action
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hl.snoozeloo.ui.theme.SnoozelooTheme
import com.hl.snoozeloo.ui.theme.alarmBackground
import com.hl.snoozeloo.ui.theme.alarmInText
import com.hl.snoozeloo.ui.theme.alarmTextColor
import com.hl.snoozeloo.ui.theme.backgroundColor
import com.hl.snoozeloo.ui.theme.cardBackground
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hl.snoozeloo.R
import com.hl.snoozeloo.domain.AlarmDetails
import com.hl.snoozeloo.ui.AlarmsUiEvents
import com.hl.snoozeloo.ui.AppViewModelProvider
import com.hl.snoozeloo.ui.SplashScreenRoot
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable
import java.time.LocalTime


@Composable
fun YourAlarmScreenRoot(
    modifier: Modifier = Modifier,
    onAddAlarmClicked: () -> Unit,
    onEditAlarmClicked: (Int) -> Unit,
    vm: YourAlarmsScreenViewModel = viewModel(factory = AppViewModelProvider.Factory) // Temporary for Testing. To Delete
) {
    val snackBarState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    val state by vm.uiState.collectAsStateWithLifecycle()

    // Channel - 4 - Listen to the event stream
    LaunchedEffect(Unit) {
        vm.eventFlow.collect { event ->
            when (event) {
                is AlarmsUiEvents.ShowSnackBar -> {
                    snackBarState.showSnackbar(event.message)
                }
            }
        }
    }

    if (state.isLoading) {
        //SplashScreenRoot()
        YourAlarmScreen(
            modifier = modifier,
            snackBarState = snackBarState,
            scope = scope,
            state = state,
            onAction = { action ->
                when(action) {
                    is YourAlarmsScreenAction.addAlarmClicked -> {
                        onAddAlarmClicked()
                    }
                    is YourAlarmsScreenAction.onAlarmClicked -> {
                        onEditAlarmClicked(action.id)
                    }
                    else -> vm.onAction(action)
                }
            }
        )
    } else {
        YourAlarmScreen(
            modifier = modifier,
            snackBarState = snackBarState,
            scope = scope,
            state = state,
            onAction = { action ->
                when(action) {
                    is YourAlarmsScreenAction.addAlarmClicked -> {
                        onAddAlarmClicked()
                    }
                    is YourAlarmsScreenAction.onAlarmClicked -> {
                        onEditAlarmClicked(action.id)
                    }
                    else -> vm.onAction(action)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourAlarmScreen(
    modifier: Modifier = Modifier,
    snackBarState: SnackbarHostState,
    scope: CoroutineScope,
    state: YourAlarmUiState,
    onAction: (YourAlarmsScreenAction) -> Unit,
) {
    Scaffold(
        modifier = modifier.padding(top = 16.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Alarms",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(YourAlarmsScreenAction.addAlarmClicked)
                },
//                    {
//                    scope.launch {
//                        snackBarState.showSnackbar(
//                            message = "Clicked FAB"
//                        )
//                    }
//                },
                shape = CircleShape,
                containerColor = backgroundColor,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = { SnackbarHost(hostState = snackBarState)}
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = alarmBackground)
        ) {
            when {
                state.alarms.isEmpty() -> {
                    EmptyScreen()
                }
                else -> {
                    // Best Practice: Use LazyColumn for lists
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = alarmBackground),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // In a real app, this would be: items(alarms) { alarm -> AlarmCard(alarm) }
                        items(state.alarms) { alarm ->
                            AlarmCard(alarms = alarm, onAction = onAction, modifier = Modifier.clickable {
                                onAction(YourAlarmsScreenAction.onAlarmClicked(alarm.id))
                            })
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.snoozeloo_icon_solid),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                text = "It's empty! Add the first alarm so you don't miss an important moment!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Composable
private fun AlarmCard(
    modifier: Modifier = Modifier,
    alarms: AlarmDetails,
    onAction: (YourAlarmsScreenAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(cardBackground, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top // Better for Row with Switch
    ) {
        Column(modifier = Modifier.weight(1f)) {
            AlarmName(text = alarms.alarmTitle)
            Row(verticalAlignment = Alignment.Bottom) {
                AlarmTimeDisplay(modifier = Modifier.alignByBaseline(), text = alarms.time.toUserFriendlyStringHHMM())
                Spacer(modifier = Modifier.width(4.dp))
                AlarmAMPMDisplay(modifier = Modifier.alignByBaseline(), text = alarms.time.toUserFriendlyStringAMPM())
            }
            AlarmCountdownDisplay(alarms = alarms)
        }

        // Rule: Switch should receive state from parent
        AlarmOnOffButton(alarm = alarms, onAction = onAction)
    }
}

@Composable
private fun AlarmOnOffButton(
    modifier: Modifier = Modifier,
    alarm: AlarmDetails,
    onAction: (YourAlarmsScreenAction) -> Unit) {

    Switch(
        modifier = modifier,
        checked = alarm.isEnabled,
        onCheckedChange = {
            onAction(YourAlarmsScreenAction.turnOnOrOffAlarm(alarm))
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = cardBackground,
            checkedTrackColor = backgroundColor,
            checkedBorderColor = backgroundColor,
            uncheckedThumbColor = cardBackground.copy(alpha = 0.7f),
            uncheckedTrackColor = backgroundColor.copy(alpha = 0.5f),
            uncheckedBorderColor = backgroundColor.copy(alpha = 0.0f),
        ),
        thumbContent = {
            // Even an empty Box forces the thumb to the larger size
            Box(modifier = Modifier.size(SwitchDefaults.IconSize))
        },
    )
}

@Composable
private fun AlarmName(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = alarmTextColor
    )
}

@Composable
private fun AlarmTimeDisplay(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 42.sp,
        fontWeight = FontWeight.Medium,
        color = alarmTextColor
    )
}

@Composable
private fun AlarmAMPMDisplay(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        color = alarmTextColor
    )
}

@Composable
private fun AlarmCountdownDisplay(modifier: Modifier = Modifier, alarms: AlarmDetails) {
    Text(
        modifier = modifier,
        text = "${alarms.timeLeftDescription}",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = alarmInText
    )
}

@Preview(showBackground = true)
@Composable
private fun YourAlarmScreenPreview() {
    SnoozelooTheme() {
        YourAlarmScreenRoot(
            onAddAlarmClicked = {},
            onEditAlarmClicked = {}
        )
    }
}