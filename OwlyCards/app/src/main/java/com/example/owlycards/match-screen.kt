package com.example.owlycards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.components.DottedPageIndicator
import com.example.owlycards.components.TopBarSmall
import com.example.owlycards.data.FlashcardSet
import kotlinx.coroutines.launch

@Composable
fun MatchSetView(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    val flashcards = flashcardSet.getFlashcards()

    val shuffeledQuestions = remember(flashcards) { // Randomize the order of the answers in a new list
        flashcards.shuffled() // Shuffle all the questions in the card list
    }

    var isMatching by remember { mutableStateOf<Boolean?>(null) } // Does the answer match the question?
    var won by remember { mutableStateOf(false) } // Has the user won?

    // Number of correct and wrong answers:
    var correctAnswers by remember { mutableStateOf(0) }
    var wrongAnswers by remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope() // Used to navigate in the pager

    if (won) { // If the user has won, display the winning-screen:
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // What message will be displayed:
            val owlyMessage = if (wrongAnswers == 0) {
                "Hoo-hoo! You just dominated the Matching game! You managed to complete a flawless game!"
            } else if (wrongAnswers < correctAnswers) {
                "Hoo-hoo! Great job winning the Matching game! You managed to complete the game with $correctAnswers correct and $wrongAnswers wrong matches, which is amazing!"
            } else {
                "G'hoo'd job winning the Matching game! You managed to complete the game with $correctAnswers correct and $wrongAnswers wrong matches, which leaves room for improvement!"
            }

            // Image of Owly:
            Image(
                painter = painterResource(id = R.drawable.owly), //image of Owly
                contentDescription = "Owly", //description of picture
                contentScale = ContentScale.Fit,
            )

            // The message from Owly
            Text(
                text = owlyMessage,
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // A button to Continue to the Flashcard-screen.
            Button(onClick = { navController.popBackStack() }) {
                Text("Continue")
            }
        }
    } else if (flashcards.isNotEmpty()) { // If the user hasn't won, but it isn't empty card set:
        Scaffold(
            topBar = { TopBarSmall("Match: ${flashcardSet.name}", true, navController) }
        ) { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) { // Display all content in a column and center it on the screen

                Spacer(modifier = Modifier.height(20.dp))

                // Question:
                Text(
                    text = "Question",
                    fontSize = 30.sp
                ) // Header text

                val questionPagerStatus = rememberPagerState { flashcards.size }

                // Pager to display all questions as a page
                HorizontalPager(
                    state = questionPagerStatus,
                    modifier = Modifier
                        .weight(1f) // Take up remaining height
                        .fillMaxWidth()
                        .heightIn(100.dp, 200.dp)
                ) { index ->
                    Box(
                        contentAlignment = Alignment.Center, // Centers the content
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Card( // Card for a question
                            shape = RoundedCornerShape(25.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245)),
                            border = BorderStroke(3.dp, Color(96, 67, 168)),
                            modifier = Modifier
                                .size(200.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center, // Centers the content
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text( // Display the question of the current Question-card
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    text = flashcards[index].question
                                )
                            }
                        }
                    }
                }

                // Pager dot indicator
                DottedPageIndicator(questionPagerStatus.pageCount, questionPagerStatus.currentPage)

                Spacer(modifier = Modifier.height(20.dp))

                // Answers:
                Text(
                    text = "Answers",
                    fontSize = 30.sp
                ) // Header text

                val answerPagerStatus = rememberPagerState { shuffeledQuestions.size }

                // Pager to display all answers as a page
                HorizontalPager(
                    state = answerPagerStatus,
                    modifier = Modifier
                        .weight(1f) // Take up remaining height
                        .fillMaxWidth()
                        .heightIn(200.dp, 400.dp)
                ) { index ->
                    Box(
                        contentAlignment = Alignment.Center, // Centers the content
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Card( // Card for an answer
                            shape = RoundedCornerShape(25.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245)),
                            border = BorderStroke(3.dp, Color(96, 67, 168)),
                            modifier = Modifier
                                .size(200.dp)
                        ) {
                            Box( // Box to center the content
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text( // Display the question of the current Question-card
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    text = shuffeledQuestions[index].answer
                                )
                            }
                        }
                    }
                }

                // Pager dot indicator
                DottedPageIndicator(answerPagerStatus.pageCount, answerPagerStatus.currentPage)

                Spacer(modifier = Modifier.height(20.dp))

                // Match-button
                Button(
                    modifier = Modifier.width(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(96, 67, 168)),
                    onClick = {
                        // Get the index from the current page-number
                        val questionIndex = questionPagerStatus.currentPage
                        val answerIndex = answerPagerStatus.currentPage

                        // Check if the current answer matches the current questions answer
                        if (flashcards[questionIndex].answer == shuffeledQuestions[answerIndex].answer) {
                            isMatching = true // They match
                            correctAnswers += 1 // Increase the number of correct answers by 1

                            flashcards.removeAt(questionIndex) // Remove the current question

                            // If there are more questions, update the index/page.
                            if (flashcards.isNotEmpty()) {
                                val newIndex =
                                    if (questionIndex >= flashcards.size) flashcards.size - 1 else questionIndex
                                coroutineScope.launch {
                                    questionPagerStatus.scrollToPage(newIndex) // Goto the new index/page
                                }
                            } else { // If there aren't more question, the user won
                                won = true
                            }
                        } else { // The answer doesn't match the question
                            isMatching = false
                            wrongAnswers += 1 // Increase the number of wrong answers by 1
                        }
                    }) {
                    Text(
                        text = "Match"
                    )
                }

                // Message for feedback/reponse to the user
                val message = if (isMatching == true) {
                    "Correct Match! Please Continue"
                } else if (isMatching == null) { // Not matched any answers yet
                    ""
                } else {
                    "Incorrect Match, Please Try Again!"
                }

                Text(
                    text = message,
                    color = if (isMatching == true) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) // Message for feedback/reponse to the user
            }
        }
    } else { // If it is an empty card set, and the user hasn't won:
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
    }
}