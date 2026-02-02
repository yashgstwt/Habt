package com.theo.habt.presentation.homeScreen

import ProgressMap
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theo.habt.R
import com.theo.habt.presentation.components.Face
import com.theo.habt.presentation.components.Reactions
import com.theo.habt.presentation.components.angry
import com.theo.habt.presentation.components.disappointed
import com.theo.habt.presentation.components.neutral
import com.theo.habt.presentation.components.smile
import com.theo.habt.presentation.components.superHappy




fun getReaction (percentage : Float ): Reactions {

    Log.d("percentageCount", percentage.toString())
    return when (percentage) {
        in 0f..25f             -> Reactions.ANGRY
        in 26f..50f            -> Reactions.DISAPPOINTED
        in 51f..65f            -> Reactions.NEUTRAL
        in 66f..80f            -> Reactions.SMILE
        else                 -> Reactions.SUPER_HAPPY
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreen(viewModel: HomeViewModal = hiltViewModel(), navigateToAddHabitScreen: () -> Unit = {}) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var reaction by remember { mutableStateOf(getReaction(state.habitWithCurrDateCompletionStatus)) }

    LaunchedEffect(state.habitWithCurrDateCompletionStatus) {
        reaction = getReaction(state.habitWithCurrDateCompletionStatus)
    }


    val color = when(reaction){
        Reactions.SMILE -> smile
        Reactions.ANGRY -> angry
        Reactions.DISAPPOINTED -> disappointed
        Reactions.NEUTRAL -> neutral
        Reactions.SUPER_HAPPY -> superHappy
    }

    val bgColor = animateColorAsState(
        targetValue = color,
        animationSpec = tween(1000)
    )


    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToAddHabitScreen() }) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { paddingValues ->
        val paddingValues = paddingValues
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {

            item {

                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 0,
                                topEndPercent = 0,
                                bottomEndPercent = 50,
                                bottomStartPercent = 50
                            )
                        )
                        .background(bgColor.value)
                ) {
                    Face(
                        modifier = Modifier.size(500.dp),
                        reactions = reaction
                    )
                }
            }


            items(state.habitsWithCompletions?.size ?: 0 ){ item ->
                state.habitsWithCompletions?.get(item)?.let {
                    ProgressMap(habit =  it.habit!! , completions =  it.habitCompletions ){ habitCompletion ->
                        viewModel.markAsCompleted(habitCompletion)
                    }
                }
            }
        }
    }
}

