package com.theo.habt.presentation.homeScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.presentation.components.Face
import com.theo.habt.presentation.components.Heatmap
import com.theo.habt.presentation.components.ProgressMap
import com.theo.habt.presentation.components.Reactions
import com.theo.habt.presentation.components.angry
import com.theo.habt.presentation.components.disappointed
import com.theo.habt.presentation.components.neutral
import com.theo.habt.presentation.components.smile
import com.theo.habt.presentation.components.superHappy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//@Preview(showSystemUi = true)
@Composable
fun HomeScreen(viewModel: HomeViewModal = hiltViewModel(), navigateToAddHabitScreen : () -> Unit   ){
    var reaction by remember { mutableStateOf(Reactions.SMILE) }

    val color = when(reaction){
        Reactions.SMILE -> smile
        Reactions.ANGRY -> angry
        Reactions.DISAPPOINTED -> disappointed
        Reactions.NEUTRAL -> neutral
        Reactions.SUPER_HAPPY -> superHappy
    }
viewModel.markAsCompleted()
    val reactionText = when(reaction){
        Reactions.SMILE -> "HAPPY"
        Reactions.ANGRY -> "ANGRY"
        Reactions.DISAPPOINTED -> "DISAPPOINTED"
        Reactions.NEUTRAL -> "NEUTRAL"
        Reactions.SUPER_HAPPY -> "SUPER HAPPY"
    }


    val bgcolor = animateColorAsState(
        targetValue = color ,
        animationSpec = tween(1000)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {navigateToAddHabitScreen() }) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ){ paddingValues ->


        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color.Black)) {

            Box(
                modifier = Modifier.fillMaxHeight(.3f).fillMaxWidth().clip(
                    RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomEndPercent = 50,
                        bottomStartPercent = 50
                    )
                ).background(bgcolor.value)
            ) {
                Face(
                    modifier = Modifier.size(500.dp),
                    reactions = reaction
                )
            }

            Heatmap(modifier = Modifier.padding(10.dp))


            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
            ProgressMap(modifier = Modifier.padding(10.dp))
        }
    }
}

