package com.theo.habt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theo.habt.ui.theme.blank
import com.theo.habt.ui.theme.fiftyPercent
import com.theo.habt.ui.theme.hundredPercent
import com.theo.habt.ui.theme.seventyFivePercent
import com.theo.habt.ui.theme.twentyFivePercent


@Preview(showSystemUi = true)
@Composable
fun Heatmap(modifier: Modifier= Modifier, noOfDays : Int = 30){

    Column(modifier = modifier) {

        Text("This Month's Progress", fontSize = 20.sp , modifier = Modifier.padding(horizontal = 10.dp , vertical = 5.dp), color = Color.White)

        LazyVerticalGrid(
            contentPadding = PaddingValues(10.dp),
            columns = GridCells.Fixed(7),
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.LightGray)
                .padding(5.dp),

            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            items(noOfDays) { count ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Green),
                    contentAlignment = Alignment.Center
                ){
                    Text((count+1).toString(), textAlign = TextAlign.Center , color = Color.White)
                }
            }
        }

        Row(modifier = Modifier
                .fillMaxWidth()
                .padding( 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray) ,
            horizontalArrangement = Arrangement.SpaceEvenly ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ProgressIndicator(percentage = 0, color = blank )
            ProgressIndicator(percentage = 25 , color = twentyFivePercent)
            ProgressIndicator(percentage = 50 , color = fiftyPercent)
            ProgressIndicator(percentage = 75 , color = seventyFivePercent)
            ProgressIndicator(percentage = 100 , color = hundredPercent)

        }
    }
}

@Composable
fun ProgressIndicator(color :Color = Color.Green, percentage :Int){


    Row(modifier = Modifier.padding(5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color)
        )
        Text("${percentage}%", modifier = Modifier.padding(5.dp) , textAlign = TextAlign.Center , fontSize = 15.sp )
    }

}