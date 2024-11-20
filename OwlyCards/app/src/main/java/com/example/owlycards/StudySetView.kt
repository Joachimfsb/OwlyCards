package com.example.owlycards

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.components.CardFace
import com.example.owlycards.components.DottedPageIndicator
import com.example.owlycards.components.FlashCard
import com.example.owlycards.components.OwlyComponent
import com.example.owlycards.data.Owly
import com.example.owlycards.components.TopBarSmall
import com.example.owlycards.data.Flashcard
import com.example.owlycards.data.FlashcardSet
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun StudySetView(owly: Owly, flashcardSet: FlashcardSet, navController: NavController, modifier: Modifier = Modifier) {

    // Flashcards
    val flashcards = flashcardSet.getFlashcards()

    // Owly
    var owlyMessage by remember { mutableStateOf(owly.greet()) }
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            seconds++

            // Every minute
            if (seconds > 30) {
                seconds = 0
                owlyMessage = owly.motivate()
            }
        }
    }


    // Prompts
    var promptCreateFlashcard by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    // Manual recompose
    var recompose by remember { mutableIntStateOf(0) }
    LaunchedEffect(recompose) { }


    // Content
    Scaffold(
        topBar = {
            TopBarSmall(flashcardSet.name, true, navController) {
                // Buttons
                if (!editMode) {
                    IconButton(onClick = { editMode = true }) {
                        Icon(Icons.Filled.Edit, "Edit flashcards")
                    }
                } else {
                    IconButton(onClick = { editMode = false }) {
                        Icon(Icons.Filled.Clear, "Stop editing flashcards")
                    }
                }
            }
        }
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
            ) { index ->
                if (index < flashcards.size) {
                    // Show flashcards
                    val flashcard = flashcards[index]

                    Box(modifier = Modifier.padding(40.dp)) {
                        // Remember state
                        var state by remember { mutableStateOf(CardFace.Front) }

                        // FlipCard
                        FlashCard(
                            cardNumber = index + 1,
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
                            actions = {
                                if (editMode) {
                                    IconButton(onClick = {
                                        flashcardSet.removeFlashcard(index)
                                        recompose++
                                    }) {
                                        Icon(Icons.Filled.Delete, "Delete flashcard")
                                    }
                                }
                            }
                        )

                    }
                } else {
                    // Show add button for adding flashcard
                    Box(
                        modifier = Modifier.padding(40.dp).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FlashCard(
                            showHeader = false,
                            onClick = {
                                promptCreateFlashcard = true
                            },
                            front = {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    "Create new flashcard",
                                    Modifier.size(64.dp)
                                )
                            },
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pager dot indicator
                DottedPageIndicator(pagerStatus.pageCount, pagerStatus.currentPage)

                Spacer(modifier.height(30.dp))

                OwlyComponent(owlyMessage)
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



