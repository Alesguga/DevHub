package com.devhub.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun LogoText(primaryColor: Color, secondaryColor: Color) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = secondaryColor)) {
                append("{ ")
            }
            withStyle(style = SpanStyle(color = primaryColor)) {
                append("DevHub")
            }
            withStyle(style = SpanStyle(color = secondaryColor)) {
                append(" }")
            }
        },
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,

    )
}