package com.hl.snoozeloo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hl.snoozeloo.R
import com.hl.snoozeloo.ui.theme.SnoozelooTheme
import com.hl.snoozeloo.ui.theme.backgroundColor

@Composable
fun SplashScreenRoot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.snoozeloo_icon),
            contentDescription = null,
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SplashScreenRootPreview() {
    SnoozelooTheme() {
        SplashScreenRoot(
            modifier = Modifier
        )
    }
}