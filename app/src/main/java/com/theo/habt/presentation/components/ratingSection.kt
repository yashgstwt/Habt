package com.theo.habt.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.apply
import kotlin.math.abs

enum class Reactions {
    SMILE, ANGRY, DISAPPOINTED, NEUTRAL,SUPER_HAPPY
}
data class EyeSize(val eyeHeight: Dp, val eyeWidth: Dp)
data class MouthSize(val width: Dp, val depth: Dp , val stroke: Dp = 20.dp)

fun eyeSize(reactions: Reactions): EyeSize {
    return when (reactions) {
        Reactions.SMILE -> EyeSize(120.dp, 120.dp)
        Reactions.DISAPPOINTED -> EyeSize(50.dp, 100.dp)
        Reactions.SUPER_HAPPY -> EyeSize(120.dp, 120.dp)
        Reactions.ANGRY -> EyeSize(100.dp, 100.dp)
        Reactions.NEUTRAL -> EyeSize(50.dp, 100.dp)
    }
}

fun mouthReactionSize(reactions: Reactions): MouthSize {
    return when (reactions) {
        Reactions.SMILE -> MouthSize(width = 100.dp, depth = 70.dp)
        Reactions.DISAPPOINTED -> MouthSize(width = 150.dp, depth = (-50).dp)
        Reactions.SUPER_HAPPY -> MouthSize(width = 150.dp, depth = 100.dp)
        Reactions.ANGRY -> MouthSize(width = 50.dp, depth = (-25).dp)
        Reactions.NEUTRAL -> MouthSize(width = 150.dp, depth = 0.dp)
    }
}

val smile = Color(0xFFFFC107)
val  neutral = Color(0xFFB4B4B4)
val disappointed = Color(0xFFFF9800)
val  superHappy = Color(0xFFFFEB3B)
val angry = Color(0xFFE37066)

@Preview(showSystemUi = true)
@Composable
fun Face(modifier: Modifier = Modifier, reactions: Reactions = Reactions.SMILE, eyeMouthSpace : Dp = 30.dp) {

    val smileSize = mouthReactionSize(reactions)
    val eyeSize = eyeSize(reactions)

    val smileLength by animateDpAsState(
        targetValue = smileSize.width,
        animationSpec = tween(1000),
        label = "smileLength"
    )
    val smileStroke  by animateDpAsState(
        targetValue = smileSize.stroke,
        animationSpec = tween(1000),
        label = "smileLength"
    )

    val smileDepth by animateDpAsState(
        targetValue = smileSize.depth,
        animationSpec = tween(1000),
        label = "smileDepth"
    )

    val eyeHeight by animateDpAsState(
        targetValue = eyeSize.eyeHeight,
        animationSpec = tween(durationMillis = 1000),
        label = "eyeHeight"
    )
    val eyeWidth by animateDpAsState(
        targetValue = eyeSize.eyeWidth,
        animationSpec = tween(durationMillis = 1000),
        label = "eyeWidth"
    )


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.7f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .height(eyeHeight)
                    .width(eyeWidth)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.Black)
            )
            Box(
                modifier = Modifier
                    .height(eyeHeight)
                    .width(eyeWidth)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.Black)
            )
        }
        Spacer(modifier = Modifier.height(eyeMouthSpace))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .weight(.3f)
        ) {
            val startX = (smileLength / 2).toPx()
            val startY = if (smileDepth <= 0.dp) abs(smileDepth.value).dp.toPx() else 0.dp.toPx()
            val endX = startX
            val endY = startY
            val centerDepth =  smileDepth.toPx()

            val path = Path()
                .apply {
                    moveTo(center.x - startX, startY)
                    cubicTo(
                        x1 = center.x - startX,
                        y1 = startY,
                        x2 = center.x,
                        y2 = centerDepth,
                        x3 = center.x + endX,
                        y3 = endY
                    )

                }
            drawPath(
                color = Color.Black,
                path = path,
                style = when (reactions){
                    Reactions.DISAPPOINTED, Reactions.SUPER_HAPPY -> {
                        Fill
                    }
                    else -> {
                        Stroke(width = smileStroke.toPx(), cap = StrokeCap.Round)
                    }
                }
            )
        }
    }
}
