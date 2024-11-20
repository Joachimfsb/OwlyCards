package com.example.owlycards.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The following code is inspired by a solution by this post https://stackoverflow.com/questions/68044576/how-to-make-flipcard-animation-in-jetpack-compose

/**
 * Allows you to determine the current face of a card.
 */
enum class CardFace(val angle: Float) {
    Front(0f) { // 0 degrees
        override val next: CardFace
            get() = Back // Opposite
    },
    Back(180f) { // 180 degrees
        override val next: CardFace
            get() = Front // Opposite
    };

    abstract val next: CardFace // Use to flip
}

/**
 * FlipCard component. Can be used with or without flip functionality.
 * Displays a 2-sided card that can be flipped (with animation).
 */
@Composable
fun FlashCard(
    cardNumber: Int = 0,
    cardFace: CardFace = CardFace.Front,
    showHeader: Boolean = true,
    onClick: (CardFace) -> Unit = {},
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    // Animate rotation
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ), label = ""
    )
    // Card
    Card(
        onClick = { onClick(cardFace) },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(3.dp, Color(96, 67, 168)),
        colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245)),
        modifier = Modifier.graphicsLayer { rotationY = rotation.value; cameraDistance = 12f * density }
    ) {
        // Add padding to bottom to center text better (if header is shown)
        val bottomPadding = if (showHeader) 30.dp else 0.dp

        // Show front of card if rotation is less than or equal to 90 degrees
        if (rotation.value <= 90f) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showHeader) {
                    // Header text and actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth().height(80.dp)
                    ) {
                        Text(
                            "Question $cardNumber",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        actions()
                    }
                }
                // Front content
                Box(
                    Modifier.fillMaxSize().padding(18.dp).padding(bottom = bottomPadding),
                    contentAlignment = Alignment.Center
                ) {
                    front()
                }
            }
        } else { // Otherwise show back of card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showHeader) {
                    // Header text
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth().height(80.dp)
                    ) {
                        Text(
                            "Answer",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .graphicsLayer {
                                    rotationY = 180f
                                }
                        )
                    }
                }
                // Back content
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                        .padding(18.dp).padding(bottom = bottomPadding),
                    contentAlignment = Alignment.Center
                ) {
                    back()
                }
            }

        }
    }
}