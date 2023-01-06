package com.hjq.sideeffect

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(onTimeOut: () -> Unit) {
    val currentOnTimeOut by rememberUpdatedState(newValue = onTimeOut)
    LaunchedEffect(Unit) {
        Log.d("huang", "LaunchedEffect")

        repeat(10) {
            delay(1000)
            Log.d("huang", "delay==$it s")
        }
        currentOnTimeOut()
    }
}


@Composable
fun RememberUpdatedStateSample() {

    val onTimeOut1: () -> Unit = { Log.d("huang", "landing 1") }
    val onTimeOut2: () -> Unit = { Log.d("huang", "landing 2") }

    val changeOnTimeOutState = remember {
        mutableStateOf(onTimeOut1)
    }
    Column {
        Button(onClick = {
            if (changeOnTimeOutState.value == onTimeOut1) {
                changeOnTimeOutState.value = onTimeOut2
            } else {
                changeOnTimeOutState.value = onTimeOut1
            }

        }) {
            Text(text = "Choose onTimeOut ${if (changeOnTimeOutState.value == onTimeOut1) 1 else 2}")
        }
        LandingScreen(changeOnTimeOutState.value)

    }
}