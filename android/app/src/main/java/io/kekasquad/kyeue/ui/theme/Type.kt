package io.kekasquad.kyeue.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.kekasquad.kyeue.R

private val CourierFontFamily = FontFamily(
    Font(R.font.courier_prime),
    Font(R.font.courier_prime_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = CourierFontFamily,
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)