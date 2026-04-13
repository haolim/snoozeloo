package com.hl.snoozeloo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hl.snoozeloo.data.local.AlarmRepository
import com.hl.snoozeloo.ui.addeditalarmscreen.AddEditAlarmScreenRoot
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmScreenRoot
import com.hl.snoozeloo.ui.theme.SnoozelooTheme
import com.hl.snoozeloo.ui.youralarmscreen.YourAlarmsScreenViewModel
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val viewModel: YourAlarmsScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SnoozelooTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    YourAlarmScreenRoot(modifier = Modifier
//                        .padding(innerPadding))
                    AddEditAlarmScreenRoot(modifier = Modifier
                        .padding(innerPadding))
                }
            }
        }
    }
}
