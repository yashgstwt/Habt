package com.theo.habt.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theo.habt.dataLayer.localDb.HabitCompletion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar


var CompletedTaskList  = MutableStateFlow(

    mutableListOf(
    HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = false, completionDate =1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731508721000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731708750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 1731908750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = false, completionDate =1731208750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 11739408750000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = false, completionDate = 11731408000000L,
        habitId = 2
    ),   HabitCompletion(
        id = 1, isCompleted = true, completionDate = 11731401750000L,
        habitId = 2
    )
    )
)


fun getHeatmapForMonthArray(
    completedList: List<HabitCompletion>, // from DB
    year: Int,
    month: Int
): BooleanArray {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1)
    val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // ✅ Step 1: Create BooleanArray with default false
    val daysArray = BooleanArray(maxDays) { false }

    // ✅ Step 2: Mark completed days as true
    completedList.forEach { habit ->
        calendar.timeInMillis = habit.completionDate
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        // -1 because array index starts at 0
        daysArray[dayOfMonth - 1] = true
    }

    return daysArray
}



fun addCompletion(habit : HabitCompletion){

    CompletedTaskList.value.add(habit)
    Log.d("Complitionlist" , CompletedTaskList.value.toString() )
}

@Preview(showSystemUi = false )
@Composable
fun ProgressMap(modifier : Modifier = Modifier, noOfDays:Int = 31 , habitName :String = "something"){
    LaunchedEffect(true) {


    }
    val complitions = CompletedTaskList.collectAsState()
    Column (  modifier = modifier
        .fillMaxWidth()
        .height(240.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(Color.LightGray)
        .padding(10.dp)
        ){

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(top = 0.dp , bottom =  25.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(habitName, fontSize = 25.sp , modifier = Modifier , color = Color.White)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "mark as done",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.Green).padding(5.dp)
                    .clickable(
                        onClick = {
                            addCompletion(HabitCompletion(
                                id = 2000, isCompleted = true, completionDate = 1L,
                                habitId = 2
                            ))
                        }
                    )
            )

        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            val lis = getHeatmapForMonthArray(complitions.value, 2025, 11)
            var dayCount = 1
            items(lis.size) { count->
                val color = if (lis[count]) Color.Green else Color.Gray

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color),
                    contentAlignment = Alignment.Center
                ){
                    Text((dayCount).toString(), textAlign = TextAlign.Center , color = Color.White)
                }
                dayCount++
            }
        }
    }
}