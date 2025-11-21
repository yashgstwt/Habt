import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * A GitHub-style heatmap that displays completions over time.
 *
 * @param modifier The modifier to be applied to the component.
 * @param startDate The first day for which data is available. The heatmap will calculate the empty
 *                  days of the week before this date to align the columns correctly.
 * @param completions A list of booleans representing completion status for each consecutive day
 *                    starting from `startDate`.
 * @param completedColor The color to use for a completed day.
 */
@Composable
fun Heatmap(
    modifier: Modifier = Modifier,
    startDate: LocalDate,
    completions: List<Boolean>,
    completedColor: Color
) {
    // A day is represented by a Boolean?
    // true = completed, false = not completed, null = empty placeholder
    val days: List<Boolean?>

    // --- 1. Data Preparation: The most important step ---
    // We need to add null placeholders for the empty days at the start of the first week.
    // DayOfWeek returns 1 for Monday, 7 for Sunday.
    val paddingDays = startDate.dayOfWeek.value - 1

    // Create a new list with nulls for padding, followed by the actual completion data.
    val paddedCompletions = List(paddingDays) { null } + completions

    // Chunk the padded list into weeks (columns). Each chunk will be a list of 7 days.
    val weeks = paddedCompletions.chunked(7)

    // --- 2. Build the UI ---
    // A Row that can scroll horizontally contains all our week columns.
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState()) // KEY: Makes the content scroll left and right
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp) // Space between each week column
    ) {
        // For each chunk of 7 days...
        weeks.forEach { week ->
            // ...create a Column to stack the 7 days vertically.
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp) // Space between each day box
            ) {
                // For each day in the week...
                week.forEach { dayCompletion ->
                    val color = when (dayCompletion) {
                        true -> completedColor  // Day was completed
                        false -> Color.Gray     // Day was not completed
                        null -> Color.Transparent // An empty placeholder day
                    }

                    // A simple Spacer is the most performant way to draw a colored box.
                    Spacer(
                        modifier = Modifier
                            .size(20.dp) // The size of each day's box
                            .clip(RoundedCornerShape(5.dp))
                            .background(color)
                    )
                }
            }
        }
    }
}