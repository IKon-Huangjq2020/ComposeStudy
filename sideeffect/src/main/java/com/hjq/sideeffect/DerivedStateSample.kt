package com.hjq.sideeffect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

fun String.containsWord(list: List<String>): Boolean {
    for (item in list) {
        if (this.contains(item)) return true
    }
    return false
}

@Composable
fun TodoList(
    highListWords: List<String> = listOf("Review", "Unblock", "Compose")
) {
    val todoTasks = remember {
        mutableStateListOf<String>()
    }
    val highListWordsTasks = remember(todoTasks, highListWords) {
        derivedStateOf {
            todoTasks.filter {
                it.containsWord(highListWords)
            }
        }
    }

    var (text, setText) = remember {
        mutableStateOf("")
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = text, onValueChange = setText)
            Button(onClick = {
                todoTasks.add(text)
                setText("")
            }) {
                Text(text = "Add")
            }
        }

        LazyColumn {
            items(highListWordsTasks.value) {
                ItemText(
                    text = it,
                    modifier = Modifier.background(Color.LightGray)
                )
            }
            items(todoTasks) {
                ItemText(text = it)
            }
        }

    }
}

@Composable
fun ItemText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = modifier
            .height(30.dp)
            .fillMaxWidth()
    )
}

@Composable
fun DerivedStateSample() {
    TodoList()
}