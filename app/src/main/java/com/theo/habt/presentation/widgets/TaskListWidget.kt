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
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.layout.Alignment
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.theo.habt.R


class TaskListWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {

            @Composable
            fun TaskItem(
                bgColor: Color = Color.Black,
                iconBgColor: Color = Color.Green,
                checkBgColor: Color = Color.Green,
                taskText: String
            ) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .cornerRadius(35.dp)
                        .padding(horizontal = 8.dp , vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Icon (Hand/Stop)

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
                                .background(iconBgColor)
                                .cornerRadius(25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.android),
                                contentDescription = "Task Icon",
                                modifier = GlanceModifier.size(24.dp),
                                colorFilter = ColorFilter.tint(GlanceTheme.colors.surface)
                            )
                        }

                        // Middle Text
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

                    // Right Icon (Checkmark)
                    Box(
                        modifier = GlanceModifier
                            .size(50.dp)
                            .background(checkBgColor)
                            .cornerRadius(25.dp)
                            .clickable {
                                // Handle click (e.g., ActionCallback)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.trending_up),
                            contentDescription = "Complete",
                            modifier = GlanceModifier.size(24.dp),
                            colorFilter = ColorFilter.tint(ColorProvider(Color.Black))
                        )
                    }
                }
                Spacer(modifier = GlanceModifier.size(10.dp))

            }

            Scaffold(
                backgroundColor = GlanceTheme.colors.surface,
                titleBar = {
                    TitleBar(
                        startIcon = ImageProvider(resId = R.drawable.android),
                        title = "Habt",
                        textColor = GlanceTheme.colors.onSurface
                    )
                }
            ) {
                LazyColumn (modifier = GlanceModifier.fillMaxSize()) {
                    items(5){ i->

                        TaskItem(taskText = "TAsk $i")

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

