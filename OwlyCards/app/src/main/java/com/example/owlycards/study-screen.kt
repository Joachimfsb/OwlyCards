package com.example.owlycards

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.data.FlashcardSet

@Preview(showBackground = true)
@Composable
fun StudySetPreview(){

}

@Composable
fun StudySetView(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    StudySetViewPage(navController, flashcardSet, modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun StudySetViewPage(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    var currentCard by remember { mutableStateOf(0) } //the current card user is at
    var flip by remember { mutableStateOf(false) } //if the card is flipped or not


    val flashcards = flashcardSet.getFlashcards()

    if (flashcards.size == 0) {
        Text("Something went wrong, there are no flashcards for this set")
    } else {
        Box(
            modifier = modifier.fillMaxSize().background(Color.DarkGray)
                .wrapContentSize(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "Study Flashcard Set:\n           ${flashcardSet.name}", //header text
                    color = Color.White,
                    fontSize = 30.sp
                )
                Box(//this white box represents the users card
                    modifier = Modifier
                        .size(400.dp)
                        .background(Color.White) //has white background color
                        .wrapContentSize(Alignment.Center) //with centered text inside the "card"
                ) {
                    Text( //decide what text is displayed pased on flip's value
                        fontSize = 20.sp,
                        text = if (!flip) {
                            flashcards[currentCard].question
                        } else {
                            flashcards[currentCard].answer
                        }
                    )
                }
                Text( //text displaying which card user is on out of all the cards in the set
                    text = "${currentCard + 1} / ${flashcards.size}",
                    color = Color.White
                )
                Row {
                    Button(
                        modifier = Modifier.width(120.dp).padding(8.dp),
                        onClick = {
                            if (flip) { //"flips" card back to question
                                flip = false
                            }
                            if (currentCard - 1 < 0) { //goes to the last card if user is at first card
                                currentCard = flashcards.size - 1
                            } else { //goes to the previous card in card set
                                currentCard--
                            }
                        }) {
                        Text(
                            text = "Previous"
                        )
                    }
                    Button(
                        modifier = Modifier.width(120.dp).padding(8.dp),
                        onClick = {
                            if (flip) { //"flips" card back to question
                                flip = false
                            }
                            if (currentCard + 1 >= flashcards.size) { //goes to first card is user is at last
                                currentCard = 0
                            } else { //goes to next card
                                currentCard++
                            }
                        }) {
                        Text(
                            text = "Next"
                        )
                    }
                }
                Row {
                    Button( //goes back to previous menu
                        modifier = Modifier.width(120.dp).padding(8.dp),
                        onClick = {
                            navController.popBackStack()
                        }) {
                        Text(
                            text = "Back"
                        )
                    }
                    Button( //changesvalue of flip so program "flipps" the card
                        modifier = Modifier.width(120.dp).padding(8.dp),
                        onClick = {
                            flip = !flip
                        }) {
                        Text(
                            text = "Flip"
                        )
                    }
                }
            }
        }
    }
}

