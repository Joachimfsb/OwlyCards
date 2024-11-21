package com.example.owlycards.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.R
import com.example.owlycards.data.Owly
import com.example.owlycards.data.SharedViewModel

/**
 * Welcome / Setup view. This page is shown on first visit of the app. It allows the user to
 * provide their name.
 */
@Composable
fun WelcomeView(viewModel: SharedViewModel, navController: NavController, modifier: Modifier = Modifier) {

    // States
    var name by remember { mutableStateOf("") }
    var nameNotFilled by remember { mutableStateOf(false)}
    var step by remember { mutableIntStateOf(1)}

    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            // Image of owly and title
            Spacer(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.owly), //image of Owly
                contentDescription = "Owly", //description of picture
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome to OwlyCards!", //header text
                fontSize = 28.sp, //text size
            )

            // Two steps
            if (step == 1) {
                // Step 1
                Spacer(modifier = Modifier.height(20.dp))
                // Owly message
                Text(
                    text = "I am thrilled to finally meet you \uD83D\uDE04",
                    fontSize = 18.sp, //text size
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "My name is Owly, and I run this place.", //header text
                    fontSize = 18.sp, //text size
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Before you begin studying, I would love to get your name.", //header text
                    fontSize = 18.sp, //text size
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
                // Name input field
                TextField(
                    value = name,
                    onValueChange = { n ->
                        // Max length
                        if (n.length <= 30) {
                            name = n.replace("\n", "") // No newlines
                        }
                                    },
                    maxLines = 1,
                    label = { Text("Name") },
                    isError = nameNotFilled,
                    supportingText = {
                        if (nameNotFilled) {
                            Text("Please type your name")
                        }
                    }
                )

                // Continue button
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val trimmedName = name.trim()
                    // Verify that name is filled out
                    if (trimmedName.isEmpty()) {
                        // Show error
                        nameNotFilled = true
                    } else {
                        name = trimmedName
                        // Go to step 2
                        step = 2
                    }
                }) {
                    Text("Continue")
                }

            } else if (step == 2) {
                // Step 2

                // Message from Owly
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Excellent, $name! You are now ready to use the app. Have fun \uD83D\uDE80",
                    fontSize = 18.sp, //text size
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Begin button (takes you to list of flashcard sets (home)
                Button(onClick = {
                    viewModel.config.name = name
                    viewModel.config.setupComplete = true
                    // When updating name, owly needs to be regenerated
                    viewModel.owly = Owly(name)
                    navController.navigate("cards_sets")
                }) {
                    Text("Begin")
                }
            }
        }
    }
}