package com.theo.habt.presentation.addnewHabitScreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.isPm
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.theo.habt.ui.theme.colorPallet
import com.theo.habt.dataLayer.constants.habitIcons
import com.theo.habt.presentation.components.TimePicker
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun AddNewHabit(modifier: Modifier = Modifier){
    var selectedColor by remember { mutableStateOf<Color>(colorPallet[0]) }
    var selectedIcon by remember { mutableStateOf(habitIcons[0]) }
    var habitName by remember { mutableStateOf("") }
    var selectedTime: TimePickerState by remember { mutableStateOf(TimePickerState(initialHour = 12 , initialMinute = 0 , is24Hour = false)) }
    var checked by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState()
    var showColorBottomSheet by remember { mutableStateOf(false) }
    var showIconBottomSheet by remember { mutableStateOf(false)   }
    var showTimePicker by remember { mutableStateOf(false)   }

    Scaffold { contentPadding ->
        // Screen content
        Column(modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(contentPadding)) {


            Text("Habit Name" , color = Color.White , fontSize = 20.sp , modifier = Modifier.padding(10.dp) )

            TextField(
                value = habitName,
                onValueChange = { newText -> habitName = newText },
                label = { Text("Enter your Habit Name") },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly ,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row (
                    horizontalArrangement = Arrangement.Absolute.Center ,
                    verticalAlignment = Alignment.CenterVertically ,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.DarkGray)
                        .padding(horizontal = 10.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                showIconBottomSheet = true
                            }
                        )

                ){

                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(horizontal = 10.dp),
                        contentDescription = "habit logo",
                        tint = Color.White,
                        imageVector = selectedIcon
                    )

                    Text(
                        text="Icon" ,
                        color = Color.White ,
                        fontSize = 20.sp ,
                    )
                }

                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.DarkGray)
                        .clickable(
                            enabled = true,
                            onClick = {
                                showColorBottomSheet = true
                            }
                        )
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   Box(modifier = Modifier
                       .size(50.dp)
                       .padding(5.dp)
                       .clip(RoundedCornerShape(15.dp))
                       .background(selectedColor))

                    Text(
                        text="Color" ,
                        color = Color.White ,
                        fontSize = 20.sp ,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)

                    )
                }
            }


        if (showColorBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showColorBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = Color.Black
            ) {

                Text(
                    text = "Choose color" ,
                    color = Color.White ,
                    fontSize = 20.sp ,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.FixedSize(50.dp) ,
                    contentPadding = PaddingValues(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center
                ){
                    items (colorPallet){ color ->

                        val isSelected = selectedColor == color

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedColor = color
                                }
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            )
                        }
                    }
                }
            }


        if (showIconBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showIconBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = Color.Black
            ) {

                Text(
                    text = "Choose Icons" ,
                    color = Color.White ,
                    fontSize = 20.sp ,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.FixedSize(50.dp) ,
                    contentPadding = PaddingValues(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center
                ){
                    items (habitIcons){ icon ->

                        val isSelected = selectedIcon == icon

                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(Color.DarkGray)
                                    .clickable {
                                        selectedIcon = icon
                                    },
                                imageVector = icon,
                                tint = Color.White,
                                contentDescription = "icon"
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set Reminder" ,
                        color = Color.White ,
                        fontSize = 20.sp, textAlign = TextAlign.Center)
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        }
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = true,
                        onClick = { showTimePicker = true }
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set Time" , color = Color.White , fontSize = 20.sp, textAlign = TextAlign.Center)
                    Text(
                        text = String.format("%02d:%02d %s", selectedTime.hour,selectedTime. minute, if (selectedTime.isPm ) "PM" else "AM"   ) ,
                        color = Color.White ,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(selectedColor)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )

                }
            }

            if (showTimePicker) {
                Dialog(onDismissRequest = { showTimePicker = false}) {
                    TimePicker(
                        onDismiss = {
                            showTimePicker = false
                        },
                        onConfirm = { time ->
                            selectedTime = (time as TimePickerState?)!!
                            showTimePicker = false
                        },
                    )
                }
            }
        }
    }
}