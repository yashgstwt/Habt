package com.theo.habt.presentation.addnewHabitScreen

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theo.habt.Util.isNotificationsEnabled
import com.theo.habt.dataLayer.constants.HabitFrequencyType
import com.theo.habt.ui.theme.colorPallet
import com.theo.habt.dataLayer.constants.habitIcons
import com.theo.habt.notificationService.HabitScheduler
import com.theo.habt.presentation.components.CongratulationsCard
import com.theo.habt.presentation.components.TimePicker
import com.theo.habt.presentation.components.VerticalNumberPicker
import com.theo.habt.ui.theme.HabitTextField
import com.theo.habt.ui.theme.borderColor

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun AddNewHabit(modifier: Modifier = Modifier, viewModal: NewHabitViewModal = hiltViewModel()) {

    val habit by viewModal.habit.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var checked by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showColorBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showIconBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val isNotificationEnabled =
        remember(checked) { mutableStateOf(isNotificationsEnabled(context)) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isNotificationEnabled.value = isGranted
        }
    )
    val showIntervalPicker = rememberSaveable{ mutableStateOf(false) }

    Scaffold { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(contentPadding)
        ) {


            Text(
                "Habit Name",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )


            val inputText = rememberTextFieldState()

            HabitTextField(
                state = inputText,
                placeholder = "Enter Habit Name ",
                cursorColor = Color(habit.colorArgb)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.Absolute.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                0.0f to borderColor,
                                .7f to Color.Black,
                                start = Offset(10.0f, 0.0f),
                                end = Offset(100.0f, 100.0f)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(Color.DarkGray)
                        .padding(horizontal = 10.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                showIconBottomSheet = true
                            }
                        )

                ) {

                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(horizontal = 10.dp),
                        contentDescription = "habit logo",
                        tint = Color.White,
                        painter = painterResource(habitIcons.getValue(habit.icon)) //habitIcons[habit.icon]
                    )

                    Text(
                        text = "Icon",
                        color = Color.White,
                        fontSize = 20.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                0.0f to borderColor,
                                .7f to Color.Black,
                                start = Offset(10.0f, 0.0f),
                                end = Offset(100.0f, 100.0f)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(Color.DarkGray)
                        .clickable(
                            enabled = true,
                            onClick = {
                                showColorBottomSheet = true
                            }
                        )
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(habit.colorArgb))
                    )

                    Text(
                        text = "Color",
                        color = Color.White,
                        fontSize = 20.sp,
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
                        text = "Choose color",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.FixedSize(50.dp),
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(colorPallet) { color ->

                            val isSelected = Color(habit.colorArgb) == color

                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModal.updateColor(color.toArgb())
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
                        text = "Choose Icons",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    val iconList by rememberSaveable { mutableStateOf(habitIcons.toList()) }

                    LazyVerticalGrid(
                        columns = GridCells.FixedSize(50.dp),
                        contentPadding = PaddingValues(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(iconList, key = { icon -> icon.first }) { icon ->
                            val isSelected = habit.icon == icon.first

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
                                        viewModal.updateIcon(icon.first)
                                    },
                                painter = painterResource(icon.second),
                                tint = Color.White,
                                contentDescription = "icon"
                            )
                        }
                    }
                }
            }

            val interval by remember { mutableIntStateOf(0) }
            val radioOptions = HabitFrequencyType.entries
            val selectedOption = remember { mutableStateOf(radioOptions[0]) }
            Column(modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption.value),
                                onClick = {
                                    selectedOption.value = text
                                    if (selectedOption.value == radioOptions[1]) {
                                        showIntervalPicker.value = true
                                        Log.d("selectedOption" , "$showIntervalPicker and clicked ")
                                    }
                                   },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption.value),
                            onClick = null
                        )
                        Text(
                            text = text.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White
                        )

                    }
                }
            }

            val selectedDayInterval = remember { mutableIntStateOf(2) }
            if (showIntervalPicker.value) {
                Dialog(onDismissRequest = {
                    showIntervalPicker.value = false
                }) {
                    Column(
                        modifier = Modifier.clip(RoundedCornerShape(25.dp)).background(Color.DarkGray).padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Please select the interval" ,
                            fontSize = 20.sp ,
                            textAlign = TextAlign.Center ,
                            color = Color.White ,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        HorizontalDivider()
                        VerticalNumberPicker(
                            modifier = Modifier,
                            range = IntRange(2, 7),
                            initialValue = 2,
                            onValueSelected = { value ->
                                selectedDayInterval.intValue = value;
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Set Reminder",
                        color = Color.White,
                        fontSize = 20.sp, textAlign = TextAlign.Center
                    )
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        }
                    )
                }

                if (checked && isNotificationEnabled.value) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                enabled = true,
                                onClick = { showTimePicker = true }
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Set Time",
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = String.format(
                                "%02d:%02d %s",
                                habit.time?.hour ?: 12, habit.time?.minute ?: 12,
                                habit.time?.hour?.let { if (it > 12) "PM" else "AM" }),
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(habit.colorArgb))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                } else if (checked && !isNotificationEnabled.value) {
                    Log.d(
                        "check state ",
                        " inside side else  $checked  ->> ${isNotificationEnabled.value}"
                    )


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }

                    Text(
                        "Please turn on the Notification to use this feature ",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }
            }

            if (showTimePicker) {
                Dialog(onDismissRequest = { showTimePicker = false }) {
                    TimePicker(
                        onDismiss = {
                            showTimePicker = false
                        },
                        onConfirm = { time ->
                            Log.d(
                                "notificationMsg",
                                "selected time : ${time.hour} : ${time.minute} "
                            )
                            viewModal.updateReminderTime(time.hour, time.minute)
                            showTimePicker = false
                        },
                    )
                }
            }

            val showAlerts = viewModal.showAlert.collectAsStateWithLifecycle()
            val alertsText = viewModal.alertMessage.collectAsStateWithLifecycle()

            Button(
                onClick = {
                    Log.d("notificationMsg", "inside ${habit.time?.hour} :  ${habit.time?.minute} ")

                    viewModal.updateName(inputText.text.toString())
                    viewModal.insertHabit()
                    habit.time?.let {
                        Log.d("notificationMsg", "inside let block ")
                        val scheduler = HabitScheduler(context, habit.name)
                        scheduler.scheduleDailyNotification(it.hour, it.minute)
                    }
                },
                colors = ButtonColors(
                    containerColor = Color(habit.colorArgb),
                    contentColor = Color.White,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.LightGray
                )
            ) {
                Text(text = "Submit")
            }

            if (showAlerts.value) {
                BasicAlertDialog(
                    onDismissRequest = {
                        viewModal.updateShowAlert(false)
                    },
                    properties = DialogProperties(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.DarkGray)
                ) {
                    Column {
                        Text(
                            alertsText.value,
                            fontSize = 25.sp,
                            color = Color.White,
                            modifier = Modifier.padding(20.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Button(
                                onClick = {
                                    viewModal.updateShowAlert(false)
                                }
                            ) {
                                Text("Ok", color = Color.White)
                            }
                        }
                    }
                }
            }

            if (habit.showSuccessMessage) {
                AlertDialog(onDismissRequest = {
                    viewModal.updateShowSuccessMessage(false)
                }) {
                    CongratulationsCard()
                }
            }
        }
    }
}