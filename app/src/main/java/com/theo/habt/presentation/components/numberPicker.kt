package com.theo.habt.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun VerticalNumberPicker(
    modifier: Modifier = Modifier,
    range: IntRange,
    initialValue: Int = range.first,
    onValueSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val numbers = remember {
        (range.first - 1)..(range.last + 1)
    }.toList()

    val initialScrollIndex = remember {
        initialValue - (range.first - 2)
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            listState.scrollToItem(initialScrollIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                // This is a placeholder to get the height of the column
                // In a real scenario, you'd use this to dynamically adjust scroll
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .height(150.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            items(numbers) { number ->

                val isSelected = listState.firstVisibleItemIndex + 1 == numbers.indexOf(number)

                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = if (isSelected) 40.sp else 24.sp
                    ),
                    modifier = Modifier.padding(8.dp),
                    color = if (isSelected) Color.White else Color.Gray

                )
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {

        val centralItemIndex = listState.firstVisibleItemIndex + 1
        if (centralItemIndex >= 0 && centralItemIndex < numbers.size) {
            val selectedNumber = numbers[centralItemIndex]
            if (selectedNumber in range) {
                onValueSelected(selectedNumber)
            }
        }
    }
}

@Preview
@Composable
fun PreviewVerticalNumberPicker() {
    VerticalNumberPicker(range = 0..23) { selectedHour ->
        Log.d("Preview", "Selected Hour: $selectedHour")
    }
}


