package com.example.owlycards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.components.CardFace
import com.example.owlycards.components.DottedPageIndicator
import com.example.owlycards.components.FlipCard
import com.example.owlycards.data.Flashcard
import com.example.owlycards.data.FlashcardSet


@Composable
fun StudySetView(flashcardSet: FlashcardSet, navController: NavController, modifier: Modifier = Modifier) {

    val flashcards = flashcardSet.getFlashcards()

    var promptCreateFlashcard by remember { mutableStateOf(false) }


    // Content
    Scaffold(
        topBar = { SmallAppBar(flashcardSet.name, true, navController) }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
        ) {
            val pagerStatus = rememberPagerState { flashcards.size + 1 }

            HorizontalPager (
                state = pagerStatus,
                modifier = Modifier
                    .weight(1f) // Take up remaining height
                    .fillMaxWidth()
                    .heightIn(200.dp, 400.dp)
            ) { page ->
                if (page < flashcards.size) {
                    // Show flashcards
                    val flashcard = flashcards[page]

                    Box(modifier = Modifier.padding(40.dp)) {
                        // Remember state
                        var state by remember { mutableStateOf(CardFace.Front) }

                        // FlipCard
                        FlipCard(
                            cardNumber = page + 1,
                            cardFace = state,
                            onClick = {
                                state = it.next // Flip
                            },
                            back = {
                                Text(flashcard.getDisplayableAnswer(), fontSize = 18.sp)
                            },
                            front = {
                                Text(flashcard.getDisplayableQuestion(), fontSize = 18.sp)
                            },
                        )

                    }
                } else {
                    // Show add button for adding flashcard
                    Box(
                        modifier = Modifier.padding(40.dp).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                promptCreateFlashcard = true
                            },
                        ) {
                            Icon(
                                Icons.Filled.AddCircle,
                                "Create new flashcard",
                                Modifier.size(64.dp)
                            )
                        }

                    }
                }
            }


            Column(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pager dot indicator
                DottedPageIndicator(pagerStatus.pageCount, pagerStatus.currentPage)

                Spacer(modifier.height(30.dp))

                Image(
                    painter = painterResource(id = R.drawable.owly), //image of Owly
                    contentDescription = "Owly", //description of picture
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }

    if (promptCreateFlashcard) {

        var question by remember { mutableStateOf("") }
        var answer by remember { mutableStateOf("") }
        var qNotFilled by remember { mutableStateOf(false) }
        var aNotFilled by remember { mutableStateOf(false) }

        AlertDialog(
            title = { Text("Create Flashcard") },
            text = {
                Column {
                    // Question
                    TextField(
                        value = question,
                        onValueChange = { q -> question = q },
                        label = { Text("Question") },
                        isError = qNotFilled,
                        supportingText = {
                            if (qNotFilled) {
                                Text(
                                    "Please supply a question",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                    // Answer
                    TextField(
                        value = answer,
                        onValueChange = { a -> answer = a },
                        label = { Text("Answer") },
                        isError = aNotFilled,
                        supportingText = {
                            if (aNotFilled) {
                                Text(
                                    "Please supply the answer",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                }
            },
            onDismissRequest = {
                promptCreateFlashcard = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Verify
                        qNotFilled = question.isEmpty()
                        aNotFilled = answer.isEmpty()

                        if (!qNotFilled && !aNotFilled) {
                            // Create
                            flashcardSet.addFlashcard(Flashcard(question, answer))
                            promptCreateFlashcard = false
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        promptCreateFlashcard = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
        return
    }
}



