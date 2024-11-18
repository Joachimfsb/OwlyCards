package com.example.owlycards.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// This implementation is inspired by the answer from https://stackoverflow.com/questions/73416996/jetpack-compose-animated-pager-dots-indicator

@Composable
fun DottedPageIndicator(numberOfPages: Int, selectedPage: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        for (i in 0 until numberOfPages) {
            val selected = i == selectedPage
            val width: Dp by animateDpAsState(
                targetValue = if (selected) { 26.dp } else { 8.dp },
                animationSpec = tween(
                    durationMillis = 300,
                ), label = ""
            )
            val color = if (selected) { Color.Gray } else { Color.LightGray }

            Canvas(Modifier.size(width = width, height = 8.dp)) {
                drawRoundRect(
                    color = color,
                    size = Size(width.toPx(), 8.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    topLeft = Offset.Zero,
                )
            }
        }
    }
}

