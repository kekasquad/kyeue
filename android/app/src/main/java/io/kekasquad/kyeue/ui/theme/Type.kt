package io.kekasquad.kyeue.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.kekasquad.kyeue.R

private val CourierFontFamily = FontFamily(
    Font(R.font.courier_prime),
    Font(R.font.courier_prime_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = CourierFontFamily
)