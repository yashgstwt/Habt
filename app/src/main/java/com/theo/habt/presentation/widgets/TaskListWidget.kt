package com.theo.habt.presentation.widgets
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Image
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.Alignment
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.theo.habt.R
import com.theo.habt.Util.getCurrentDateInLong
import com.theo.habt.dataLayer.constants.habitIcons
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    val repo : RoomDbRepo
}

class TaskListWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {


        val entryPoint  = EntryPointAccessors.fromApplication(context,WidgetEntryPoint::class.java )
        val repo = entryPoint.repo

        val habitList = repo.getAllHabitsForWidgetWithStatus(getCurrentDateInLong())


        provideContent {

            @Composable
            fun TaskItem(
                bgColor: Color = Color.Black,
                iconBgColor: Int,
                taskText: String,
                icon: String,
                habitId: Int,
                isCompleted: Boolean,
                markASCompeted: (habitId: Int) -> Unit
            ) {

                var isCompleted  =  remember { mutableStateOf(isCompleted) }
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .cornerRadius(35.dp)
                        .padding(horizontal = 8.dp , vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row( modifier = GlanceModifier
                        .defaultWeight()
                        .height(50.dp)
                        .background(bgColor)
                        .cornerRadius(35.dp)
                        .padding(horizontal = 8.dp ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = GlanceModifier
                                .size(40.dp)
                                .background(ColorProvider(Color(iconBgColor)))
                                .cornerRadius(25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                provider = ImageProvider(habitIcons[icon]!!),
                                contentDescription = "Task Icon",
                                modifier = GlanceModifier.size(24.dp),
                                colorFilter = ColorFilter.tint(GlanceTheme.colors.surface)
                            )
                        }

                        Text(
                            text = taskText,
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 18.sp
                            ),
                            modifier = GlanceModifier
                                .padding(horizontal = 16.dp),
                        )

                    }

                    Spacer(modifier = GlanceModifier.size(10.dp))

                    Box(
                        modifier = GlanceModifier
                            .size(50.dp)
                            .background(if(isCompleted.value) GlanceTheme.colors.primary else GlanceTheme.colors.error)
                            .cornerRadius(25.dp)
                            .clickable {
                                markASCompeted(habitId)
                                if(!isCompleted.value){
                                    isCompleted.value = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.done),
                            contentDescription = "Complete",
                            modifier = GlanceModifier.size(24.dp),
                            colorFilter = ColorFilter.tint(ColorProvider(Color.Black))
                        )
                    }
                }
                Spacer(modifier = GlanceModifier.size(10.dp))

            }

            // main content for widget

            Scaffold(
                backgroundColor = GlanceTheme.colors.surface,
                titleBar = {
                    TitleBar(
                        startIcon = ImageProvider(resId = R.drawable.android),
                        title = "Today's Task",
                        textColor = GlanceTheme.colors.onSurface,
                        iconColor = GlanceTheme.colors.onSurface
                    )
                }
            ) {
                LazyColumn (modifier = GlanceModifier.fillMaxSize()) {

                    if (habitList.isNotEmpty()){

                        items(habitList){ i->

                            TaskItem(
                                iconBgColor = i.habit.colorArgb,
                                taskText = i.habit.name,
                                icon = i.habit.icon,
                                habitId = i.habit.id,
                                isCompleted = i.isCompleted
                            ) { habitId ->

                                CoroutineScope(Dispatchers.IO).launch {
                                    repo.insertHabitCompletion(HabitCompletion(habitId = habitId, isCompleted = true , completionDate = getCurrentDateInLong()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class HabtWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TaskListWidget()
}

