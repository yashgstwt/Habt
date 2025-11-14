package com.theo.habt.presentation.homeScreen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.presentation.components.Face
import com.theo.habt.presentation.components.Heatmap
import com.theo.habt.presentation.components.ProgressMap
import com.theo.habt.presentation.components.Reactions
import com.theo.habt.presentation.components.angry
import com.theo.habt.presentation.components.disappointed
import com.theo.habt.presentation.components.neutral
import com.theo.habt.presentation.components.smile
import com.theo.habt.presentation.components.superHappy



@Preview(showSystemUi = true)
@Composable
fun HomeScreen(viewModel: HomeViewModal = hiltViewModel(), navigateToAddHabitScreen : () -> Unit = {}  ){
    var reaction by remember { mutableStateOf(Reactions.SMILE) }

    val state  by  viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.habits) {
        Log.d("Habit" , state.habits.toString())
    }

    val color = when(reaction){
        Reactions.SMILE -> smile
        Reactions.ANGRY -> angry
        Reactions.DISAPPOINTED -> disappointed
        Reactions.NEUTRAL -> neutral
        Reactions.SUPER_HAPPY -> superHappy
    }
    viewModel.markAsCompleted(HabitCompletion(completionDate = 45 , habitId = 1 , isCompleted = true ))
    val reactionText = when(reaction){
        Reactions.SMILE -> "HAPPY"
        Reactions.ANGRY -> "ANGRY"
        Reactions.DISAPPOINTED -> "DISAPPOINTED"
        Reactions.NEUTRAL -> "NEUTRAL"
        Reactions.SUPER_HAPPY -> "SUPER HAPPY"
    }


    val bgColor = animateColorAsState(
        targetValue = color ,
        animationSpec = tween(1000)
    )

    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {navigateToAddHabitScreen() }) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ){ paddingValues ->


        Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth().wrapContentHeight().padding(paddingValues).background(Color.Black)) {

            Box(
                modifier = Modifier.height(300.dp).fillMaxWidth().clip(
                    RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomEndPercent = 50,
                        bottomStartPercent = 50
                    )
                ).background(bgColor.value)
            ) {
                Face(
                    modifier = Modifier.size(500.dp),
                    reactions = reaction
                )
            }

            Heatmap(modifier = Modifier.padding(10.dp).height(250.dp), noOfDays = 31)

            Button(
                onClick = {
                    viewModel.getHabits()
                    viewModel.markAsCompleted(HabitCompletion(completionDate = 45 , habitId =2 , isCompleted = true ))
                }
            ) {
                Text("fetch habits")
            }


            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
        }
    }
}

