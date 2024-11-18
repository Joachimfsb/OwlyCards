package com.example.owlycards.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The following code is inspired by a solution by this post https://stackoverflow.com/questions/68044576/how-to-make-flipcard-animation-in-jetpack-compose
enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

@Composable
fun FlipCard(
    cardNumber: Int,
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {}
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ), label = ""
    )
    Card(
        onClick = { onClick(cardFace) },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(3.dp, Color(96, 67, 168)),
        colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245)),
        modifier = Modifier.graphicsLayer { rotationY = rotation.value; cameraDistance = 12f * density }
    ) {
        if (rotation.value <= 90f) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Question $cardNumber",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 30.dp)
                )
                Box(
                    Modifier.fillMaxSize().padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    front()
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Answer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .graphicsLayer {
                            rotationY = 180f
                        }
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    back()
                }
            }

        }
    }
}