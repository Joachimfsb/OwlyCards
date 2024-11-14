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

    val shuffeledQuestions = remember(cardSet) {
        cardSet.shuffled()
    }

    var currentQuestion by remember { mutableStateOf(0) }
    var currentAnswer by remember { mutableStateOf(0) }

    var isMatching by remember { mutableStateOf<Boolean?>(null) }
    var won by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Matching Flashcard Set:\n           $flashsetName", //header text
                color = Color.White,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Question:

            Text(
                text = "Question:", //header text
                color = Color.White,
                fontSize = 30.sp
            )
            Box(//this white box represents the users card
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White) //has white background color
                    .wrapContentSize(Alignment.Center) //with centered text inside the "card"
            ) {
                Text( //decide what text is displayed pased on flip's value
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    text = cardSet[currentQuestion].first
                )
            }
            Text( //text displaying which card user is on out of all the cards in the set
                text = "${currentQuestion + 1} / ${cardSet.size}",
                color = Color.White
            )

            // Answers:

            Text(
                text = "Answers:", //header text
                color = Color.White,
                fontSize = 30.sp
            )
            Box(//this white box represents the users card
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White) //has white background color
                    .wrapContentSize(Alignment.Center) //with centered text inside the "card"
            ) {
                Text( //decide what text is displayed pased on flip's value
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    text = shuffeledQuestions[currentAnswer].second
                )
            }
            Text( //text displaying which card user is on out of all the cards in the set
                text = "${currentAnswer + 1} / ${shuffeledQuestions.size}",
                color = Color.White
            )

            // Buttons:

            Row {
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        if (currentAnswer - 1 < 0) { //goes to the last card if user is at first card
                            currentAnswer = shuffeledQuestions.size - 1
                        } else { //goes to the previous card in card set
                            currentAnswer--
                        }
                    }) {
                    Text(
                        text = "Previous"
                    )
                }
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        if (cardSet[currentQuestion].first == shuffeledQuestions[currentAnswer].first) {
                            isMatching = true
                            if (currentQuestion + 1 >= cardSet.size) {
                                won = true
                            } else { //goes to next card
                                currentQuestion++
                            }
                        } else {
                            isMatching = false
                        }
                    }) {
                    Text(
                        text = "Match"
                    )
                }
                Button(
                    modifier = Modifier.width(120.dp).padding(8.dp),
                    onClick = {
                        if (currentAnswer + 1 >= shuffeledQuestions.size) { //goes to first card is user is at last
                            currentAnswer = 0
                        } else { //goes to next card
                            currentAnswer++
                        }
                    }) {
                    Text(
                        text = "Next"
                    )
                }
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Back") //goes back to previous screen
            }

            val message: String
            if (won) {
                message = "You won the Matching-game!"
            } else if (isMatching == true) {
                message = "Correct Match! Please Continue"
            } else if (isMatching == null) {
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
            )
        }
    }
}