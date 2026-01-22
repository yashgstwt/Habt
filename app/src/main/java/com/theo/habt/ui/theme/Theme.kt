package com.theo.habt.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.BLUE_DOMINATED_EXAMPLE
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun HabtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}



@Composable
fun HabtWidgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview( name = "Dark Mode",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFF00FF00,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun prv (){
    HabitTextField()
//    Box(
//        modifier = Modifier
//            .padding(10.dp)
//            .clip(RoundedCornerShape(15.dp))
//            .background(Brush.linearGradient(
//                0.0f to Color.White,
//                .7f to Color.Blue,
//                start = Offset(100.0f, 500.0f),
//                end = Offset(1000.0f, 1000.0f)
//            ))
//            .size(300.dp,50.dp),
//        contentAlignment = Alignment.Center
//    ){}

}

@Composable
fun HabitTextField(
    modifier: Modifier = Modifier,
    fontSize : TextUnit = 15.sp,
    color :Color = Color.White,
    state : TextFieldState = TextFieldState(),
    placeholder: String = " enter text " ,
    cursorColor : Color = Color.Yellow
){

    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.DarkGray)
            .height(50.dp)
            .border(
                width = 1.dp,
                brush =  Brush.linearGradient(
                    0.0f to borderColor,
                    .7f to Color.Transparent,
                    start = Offset(100.0f, 500.0f),
                    end = Offset(1000.0f, 1000.0f)
                ),
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ){

        BasicTextField(
            state = state,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.DarkGray),
            textStyle = TextStyle().copy(fontSize = fontSize, color = color),
            lineLimits = TextFieldLineLimits.SingleLine,
            decorator = TextFieldDecorator { inner ->
                if(state.text.isEmpty()){
                    Text(placeholder , color = Color.White , fontSize = fontSize)
                }
                inner()
            },
            cursorBrush = SolidColor(cursorColor)
        )
    }
}




val textFieldTheme = TextFieldColors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.LightGray,
    disabledTextColor = Color.DarkGray,
    errorTextColor = Color.Red,
    focusedContainerColor = progressMapContainer,
    unfocusedContainerColor = progressMapContainer,
    disabledContainerColor = Color.Black,
    errorContainerColor = Color.Red,
    cursorColor = Color.White,
    errorCursorColor = Color.Red,
    textSelectionColors = TextSelectionColors(
        handleColor = Color.White,
        backgroundColor = Color.Blue.copy(alpha = .5f)
    ),
    focusedIndicatorColor = Purple40,
    unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.6f),
    disabledIndicatorColor = Color.DarkGray.copy(alpha = 0.4f),
    errorIndicatorColor = Color.Red,
    focusedLeadingIconColor = Color.White,
    unfocusedLeadingIconColor = Color.LightGray,
    disabledLeadingIconColor = Color.DarkGray,
    errorLeadingIconColor = Color.Red,
    focusedTrailingIconColor = Color.White,
    unfocusedTrailingIconColor = Color.LightGray,
    disabledTrailingIconColor = Color.DarkGray,
    errorTrailingIconColor = Color.Red,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.LightGray,
    disabledLabelColor = Color.DarkGray,
    errorLabelColor = Color.Red,
    focusedPlaceholderColor = Color.White.copy(alpha = 0.75f),
    unfocusedPlaceholderColor = Color.LightGray.copy(alpha = 0.6f),
    disabledPlaceholderColor = Color.DarkGray.copy(alpha = 0.45f),
    errorPlaceholderColor = Color.Red.copy(alpha = 0.9f),
    focusedSupportingTextColor = Color.White,
    unfocusedSupportingTextColor = Color.LightGray,
    disabledSupportingTextColor = Color.DarkGray,
    errorSupportingTextColor = Color.Red,
    focusedPrefixColor = Color.White,
    unfocusedPrefixColor = Color.LightGray,
    disabledPrefixColor = Color.DarkGray,
    errorPrefixColor = Color.Red,
    focusedSuffixColor = Color.White,
    unfocusedSuffixColor = Color.LightGray,
    disabledSuffixColor = Color.DarkGray,
    errorSuffixColor = Color.Red
)