import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theo.habt.dataLayer.constants.habitIcons
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime


@Composable
fun ProgressMap(
    modifier: Modifier = Modifier,
    habit: Habit = Habit(name = "some" , colorArgb = -15124 , creationDate = 5485316354, icon = ""),
    completions: List<List<Pair<Int, Boolean>>>,
    markAsComplete : (habitCompletion : HabitCompletion)-> Unit
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.DarkGray)
            .padding(10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource( habitIcons.getValue(habit.icon)),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(habit.colorArgb))
                        .padding(5.dp)
                )
                Text(habit.name, fontSize = 25.sp, color = Color.White)
            }

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "mark as done",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(habit.colorArgb))
                    .clickable {
                        markAsComplete(HabitCompletion(habitId = habit.id , isCompleted = true , completionDate = LocalDate.now(ZoneId.systemDefault()).toEpochDay()))
                        Log.d("today'sDate","In days"+LocalDate.now(ZoneId.systemDefault()).toEpochDay().toString() )
                        Log.d("today'sDate","In formated "+  LocalDate.ofEpochDay(LocalDate.now(ZoneId.systemDefault()).toEpochDay()).dayOfMonth)
                    }
            )
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            completions.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround ,
                ) {
                    rowItems.forEach { (day, isCompleted) ->

                        val color = if (isCompleted) Color(habit.colorArgb) else Color.Gray
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(10.dp)).background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day.toString(), textAlign = TextAlign.Center, color = Color.White)
                        }
                    }
                    repeat(8 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}