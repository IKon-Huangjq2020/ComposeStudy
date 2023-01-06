package com.hjq.sideeffect

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*


@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher,
    onBack: () -> Unit
) {

    val currentCallback by rememberUpdatedState(newValue = onBack)

    val backCallBack = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentCallback()
            }
        }
    }
    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallBack)
        onDispose {
            Log.d("huang", "onDispose")
            backCallBack.remove()
        }
    }
}

@Composable
fun DisposableEffectSample(backDispatcher: OnBackPressedDispatcher) {
    var addBackCallBack by remember {
        mutableStateOf(false)
    }

    Row {
        Switch(
            checked = addBackCallBack,
            onCheckedChange = {
                addBackCallBack = !addBackCallBack
            })
        Text(text = if (addBackCallBack) "Add Back CallBack" else "Not Add Back Callback")
    }

    if (addBackCallBack) {
        BackHandler(backDispatcher = backDispatcher) {
            Log.d("huang", "BackHandler back")
        }
    }
}

