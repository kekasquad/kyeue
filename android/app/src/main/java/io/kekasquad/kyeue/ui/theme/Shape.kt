package io.kekasquad.kyeue.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
)

val StudentShape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
val TeacherShape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
val ChipShape = RoundedCornerShape(16.dp)