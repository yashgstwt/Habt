package com.theo.habt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun CongratulationsCard (modifier: Modifier = Modifier , msg: String = "Awesome! A new journey begins. Stick with it!" ){

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Color.DarkGray)
            .padding(10.dp , 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "achievement" ,
            modifier = Modifier.size(80.dp),
            tint = Color.Yellow
        )
        Text(
            text = msg ,
            fontSize = 25.sp ,
            color = Color.White ,
            modifier = Modifier.fillMaxWidth()
        )
    }
}