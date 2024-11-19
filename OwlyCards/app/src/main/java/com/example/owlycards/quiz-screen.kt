package com.example.owlycards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.components.TopBarSmall
import com.example.owlycards.data.FlashcardSet

@Composable
fun QuizScreen(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    QuizScreenPage(navController, flashcardSet, modifier)
}

@Composable
fun QuizScreenPage(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    val cardSet = remember { flashcardSet.getFlashcards().shuffled() } // shuffle flashcards

    // if there are no cards in a set, give message and back button
    if (cardSet.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Flashcard set is empty!", color = Color.Gray, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
        return
    }

    // state variables
    var currentQuestionIndex by remember { mutableStateOf(0) } // question index
    var flip by remember { mutableStateOf(false) } // flipped/revealed or not for answer
    var answer by remember { mutableStateOf("") } // user input
    var feedback by remember { mutableStateOf("") } // feedback based on wrong/right answer

    Scaffold(
        // Top bar with back button and Quiz : (name of set)
        topBar = { TopBarSmall("Quiz: ${flashcardSet.name}", true, navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .imePadding(), // adjusts when keyboard is activated
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // flashcard Surface with question/answer
            Surface(
                // rounded corners and elevation for shadow effect
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // center content inside card
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // question/answer text
                    Text(
                        text = if (!flip) "Question:" else "Answer:",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // display actual question/answer of the current question
                    Text(
                        text = if (!flip) cardSet[currentQuestionIndex].question else cardSet[currentQuestionIndex].answer,
                        color = Color.Black,
                        fontSize = 20.sp,
                        lineHeight = 30.sp
                    )
                }
            }

            // question counter (current/total questions)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${currentQuestionIndex + 1} / ${cardSet.size}",
                color = Color.Gray,
                fontSize = 16.sp
            )

            // feedback message
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedback,
                color = if (feedback == "Correct!" || feedback == "Quiz Complete!") Color(96, 167, 68) else Color.Red,
                fontSize = 16.sp
            )

            // hide input and buttons if the quiz is complete
            if (feedback != "Quiz Complete!") {
                // input box for user answer
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = answer,
                    onValueChange = { answer = it },
                    placeholder = { Text("Type your answer here") },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // reveal and submit buttons
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // show reveal button only if the user answers incorrectly
                    if (feedback == "Incorrect, try again.") {
                        Button(
                            onClick = { flip = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(96, 67, 168)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Reveal", color = Color.White)
                        }
                    }
                    // submit input
                    Button(
                        onClick = {
                            if (answer.equals(cardSet[currentQuestionIndex].answer, ignoreCase = true)) {
                                feedback = "Correct!"
                                if (currentQuestionIndex + 1 < cardSet.size) {
                                    currentQuestionIndex++
                                    flip = false // reset flip for next question
                                } else {
                                    feedback = "Quiz Complete!"
                                }
                            } else {
                                feedback = "Incorrect, try again."
                            }
                            answer = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(96, 67, 168)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Submit", color = Color.White)
                    }
                }
            }

            // back and restart buttons
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // back button, navigates one back
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Back", color = Color.Gray)
                }
                // restart
                Button(
                    onClick = {
                        currentQuestionIndex = 0 // make index 0 to restart current quiz from beginning
                        flip = false // reset flip state
                        answer = ""
                        feedback = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Restart", color = Color.Gray)
                }
            }
        }
    }
}
