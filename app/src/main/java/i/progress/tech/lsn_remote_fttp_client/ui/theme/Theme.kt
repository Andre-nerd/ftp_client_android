package i.progress.tech.lsn_remote_fttp_client.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

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
var DefaultColor = GreyNoUse
var DefaultTealColor = CianUse
var isDarkTheme = false
var ColorWhenUse = DefaultTealColor
val Neutral10 = Color(red = 28, green = 27, blue = 31)
@Composable
fun Fttp_clientTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
val styleAboutText = TextStyle(
    fontSize = fontSizeSmall.sp,
    lineHeight = 18.sp,
    fontFamily = FontFamily.SansSerif,
    textAlign = TextAlign.Justify
)
val styleAboutTextBold = TextStyle(
    fontSize = fontSizeSmall.sp,
    lineHeight = 18.sp,
    fontFamily = FontFamily.SansSerif,
    textAlign = TextAlign.Justify,
    fontWeight = FontWeight.Bold
)
val styleMinText = TextStyle(
    fontSize = fontSizeMin.sp,
    lineHeight = 12.sp,
    fontFamily = FontFamily.SansSerif,
    textAlign = TextAlign.Justify
)
val styleLargeText = TextStyle(
    fontSize = 18.sp,
    lineHeight = 48.sp,
    fontFamily = FontFamily.SansSerif,
    textAlign = TextAlign.Justify
)