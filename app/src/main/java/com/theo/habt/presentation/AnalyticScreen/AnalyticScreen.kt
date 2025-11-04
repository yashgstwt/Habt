package com.theo.habt.presentation.AnalyticScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnalyticScreen() {
    var habitName by remember { mutableStateOf("") }

    TextField(
        value = habitName,
        onValueChange = { newText -> habitName = newText },
        label = { Text("Enter your Habit Name") },
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )

}