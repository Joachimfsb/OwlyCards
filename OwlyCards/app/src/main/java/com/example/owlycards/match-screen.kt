package com.example.owlycards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MatchSetView(navController: NavController, flashsetName: String, modifier: Modifier = Modifier) {
    MatchSetViewPage(navController = navController, flashsetName, modifier = Modifier.fillMaxSize().wrapContentSize(
        Alignment.Center))
}

@Composable
fun MatchSetViewPage(navController: NavController, flashsetName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current //gets context for file manipulation
    val cardSet = LoadSet(context, flashsetName) //loads the flashcards set Q&A's into list

    val shuffeledQuestions = remember(cardSet) { // Randomize the order of the answers in a new list
        cardSet.shuffled() // Shuffle all the questions in the card list
    }

    var currentQuestion by remember { mutableStateOf(0) } // Index of the current question
    var currentAnswer by remember { mutableStateOf(0) } // Index of the current answer

    var isMatching by remember { mutableStateOf<Boolean?>(null) } // Does the answer match the question?
    var won by remember { mutableStateOf(false) } // Has the user won?

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) { // Give the background a Dark gray color
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) { // Display all content in a column and center it on the screen
            Text(
                text = "Matching Flashcard Set:\n           $flashsetName", 
                color = Color.White,
                fontSize = 30.sp
            ) // Header text

            Spacer(modifier = Modifier.height(30.dp))

            // Question:

            Text(
                text = "Question:", 
                color = Color.White,
                fontSize = 30.sp
            ) // Header text

            Box( // This white box represents the flashcard
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White) // Has white background color
                    .wrapContentSize(Alignment.Center) // With centered text inside the "card"
            ) {
                Text( // Display the question of the current Question-card
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    text = cardSet[currentQuestion].first
                )
            }

            Text( // Text displaying which card user is on out of all the cards in the set
                text = "${currentQuestion + 1} / ${cardSet.size}",
                color = Color.White
            )

            // Answers:

            Text(
                text = "Answers:",
                color = Color.White,
                fontSize = 30.sp
            ) // Header text

            Box( // This white box represents the flashcard
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White) // Has white background color
                    .wrapContentSize(Alignment.Center) // With centered text inside the "card"
            ) {
                Text( // Display the answer of the current Answer-card
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    text = shuffeledQuestions[currentAnswer].second
                )
            }

            Text( // Text displaying which card user is on out of all the cards in the set
                text = "${currentAnswer + 1} / ${shuffeledQuestions.size}",
                color = Color.White
            )

            // Buttons:

            Row {
                // Previous-button:
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        if (currentAnswer - 1 < 0) { // Goes to the last card if user is at first card
                            currentAnswer = shuffeledQuestions.size - 1
                        } else { // Goes to the previous card in card set
                            currentAnswer--
                        }
                    }) {
                    Text(
                        text = "Previous"
                    )
                }

                // Match-button
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        // Check if the answer is matching or not
                        if (cardSet[currentQuestion].second == shuffeledQuestions[currentAnswer].second) {
                            isMatching = true
                            // If current question is the last question, they won
                            if (currentQuestion + 1 >= cardSet.size) {
                                won = true
                            } else { // Goes to next question
                                currentQuestion++
                            }
                        } else { // Not matching
                            isMatching = false
                        }
                    }) {
                    Text(
                        text = "Match"
                    )
                }

                // Next-button
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        if (currentAnswer + 1 >= shuffeledQuestions.size) { // Goes to first card if user is at last
                            currentAnswer = 0
                        } else { // Goes to next card
                            currentAnswer++
                        }
                    }) {
                    Text(
                        text = "Next"
                    )
                }
            }

            // Back-button
            Button(onClick = { navController.popBackStack() }) {
                Text("Back") // Goes back to previous screen
            }

            // Message for feedback/reponse to the user
            val message: String
            if (won) {
                message = "You won the Matching-game!"
            } else if (isMatching == true) {
                message = "Correct Match! Please Continue"
            } else if (isMatching == null) { // Not matched any answers yet
                message = ""
            } else {
                message = "Incorrect Match, Please Try Again!"
            }

            Text(
                text = message,
                color = if (isMatching == true) Color.Green else Color.Red,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) // Message for feedback/reponse to the user
        }
    }
}