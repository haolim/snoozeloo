package com.hl.snoozeloo.ui.addeditalarmscreen

import android.R
import android.R.attr.enabled
import android.R.attr.height
import android.R.attr.onClick
import android.R.attr.singleLine
import android.R.attr.textStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hl.snoozeloo.ui.AppViewModelProvider
import com.hl.snoozeloo.ui.theme.SnoozelooTheme
import com.hl.snoozeloo.ui.theme.alarmBackground
import com.hl.snoozeloo.ui.theme.alarmInText
import com.hl.snoozeloo.ui.theme.backgroundColor
import com.hl.snoozeloo.ui.theme.closeButton

@Composable
fun AddEditAlarmScreenRoot(
    alarmId: Int? = null,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditAlarmScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    //val viewModel = viewModel<AddEditAlarmScreenViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val action = viewModel::onAction


    LaunchedEffect(state.isSaveSuccess) {
        if (state.isSaveSuccess) {
            onBackClicked()
        }
    }

    AddEditAlarmScreen(
        modifier = modifier,
        state = state,
        //focusManager = focusManager,
        onAction = action,
        onBackClicked = onBackClicked,
        alarmId = alarmId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAlarmScreen(
    modifier: Modifier = Modifier,
    state: AddEditAlarmUiState,
    //focusManager: FocusManager,
    onAction: (AddEditAlarmScreenAction) -> Unit,
    onBackClicked: () -> Unit,
    alarmId: Int? = null
) {
        Scaffold { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
               // verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TopRowCloseAndSave(
                    modifier = Modifier,
                    state = state,
                    onAction = onAction,
                    onBackClicked = onBackClicked
                )
                HourAndMinuteRow(
                    modifier = Modifier,
                    state = state,
                    onAction = onAction,
                    alarmId = alarmId
                )
                Spacer(modifier = Modifier.padding(16.dp))
                AlarmName()
        }

    }
}

@Composable
private fun TopRowCloseAndSave(
    modifier: Modifier = Modifier,
    state: AddEditAlarmUiState,
    onAction: (AddEditAlarmScreenAction) -> Unit,
    onBackClicked: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(closeButton)
                .size(32.dp, 32.dp),
            onClick = onBackClicked,
            colors = IconButtonColors(
                containerColor = closeButton,
                disabledContentColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
        Button(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(32.dp),
            colors = ButtonColors(
                containerColor = backgroundColor,
                disabledContentColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)),
            onClick = {
                onAction(AddEditAlarmScreenAction.onSaveClick)
            },
            enabled = !state.isSaving
        ) {
            Text(
                text = "Save",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun HourAndMinuteRow(
    modifier: Modifier = Modifier,
    state: AddEditAlarmUiState,
    onAction: (AddEditAlarmScreenAction) -> Unit,
    alarmId: Int? = null
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clip(shape = RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlarmTimeInputField(
            modifier = modifier.padding(16.dp),
            value = state.hourInput,
            onValueChange = { newValue ->
                onAction(AddEditAlarmScreenAction.onHourChange(newValue))
            },
            label = "HH"
        )
        Text(
            text = ":",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )
        AlarmTimeInputField(
            modifier = modifier.padding(16.dp),
            value = state.minuteInput,
            onValueChange = {newValue ->
                onAction(AddEditAlarmScreenAction.onMinuteChange(newValue))
            },
            label = "MM"
        )
    }
}

@Composable
fun AlarmTimeInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = alarmBackground,
            unfocusedContainerColor = alarmBackground,
            disabledContainerColor = alarmBackground,
        ),
        modifier = modifier.size(100.dp),
        enabled = true,
        singleLine = true,
        textStyle = MaterialTheme.typography.displayMedium.copy(
            backgroundColor,
            textAlign = TextAlign.Center)
    )
}

@Composable
private fun AlarmName(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)
        .clip(shape = RoundedCornerShape(16.dp))
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = "Alarm Name",
            onValueChange = {},
//            modifier = Modifier
//                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        OutlinedTextField(
            value = "Work",
            onValueChange = {},
//            modifier = Modifier
//                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                errorTextColor = alarmInText,
                focusedTextColor = alarmInText,
                disabledTextColor = alarmInText,
                unfocusedTextColor = alarmInText,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun AddEditAlarmScreenPreview() {
    SnoozelooTheme() {
        AddEditAlarmScreen(
            state = AddEditAlarmUiState(),
            onAction = { },
            onBackClicked = { }
        )
    }
}