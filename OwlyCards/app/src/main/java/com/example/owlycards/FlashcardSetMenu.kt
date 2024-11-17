package com.example.owlycards

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSetMenuView(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val flashcardSets = viewModel.value.getFlashcardSets()

    // The two lines below allow for manual recomposition
    var recompose by remember { mutableIntStateOf(0) } // Increment this variable when you wish to recompose
    LaunchedEffect(recompose) { } // Triggers the actual recomposition

    // Content
    Scaffold(
        topBar = { SmallAppBar("Flashcard sets", false) }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                itemsIndexed(flashcardSets.toList()) { index, flashcardSet ->
                    Surface( // Each set gets its own row which fills the width of screen
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 2.dp)
                    ) {
                        Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column { // Column containing both the name of the card set and game mode buttons
                                Text( // The name of the saved flashcard set
                                    text = flashcardSet.second.name,
                                    fontSize = 20.sp,
                                )
                                Text( // The name of the saved flashcard set
                                    text = "Number of flashcards: ${flashcardSet.second.getFlashcards().size}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                )

                                // Buttons to start the different game modes:
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    // Study button
                                    Button(
                                        modifier = Modifier.padding(end = 6.dp),
                                        onClick = {
                                            navController.navigate("study-set/${flashcardSet.second.name}")
                                        },
                                    ) {
                                        Text("STUDY")
                                    }

                                    // Match button
                                    Button(
                                        modifier = Modifier.padding(end = 6.dp),
                                        onClick = {
                                            navController.navigate("match-set/${flashcardSet.second.name}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        border = BorderStroke(2.dp, ButtonDefaults.buttonColors().containerColor)
                                    ) {
                                        Text("MATCH", color = ButtonDefaults.buttonColors().containerColor)
                                    }

                                    // Quiz button
                                    Button(
                                        onClick = {
                                            //navController.navigate("quiz-set/$setName")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        border = BorderStroke(2.dp, ButtonDefaults.buttonColors().containerColor)
                                    ) {
                                        Text("QUIZ", color = ButtonDefaults.buttonColors().containerColor)
                                    }
                                }
                            }

                            Row (
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Icon(Icons.Filled.Delete, "Delete flashcard set")
                                }
                            }
                        }
                    }
                    if (index == flashcardSets.size - 1) {
                        Spacer(Modifier.height(85.dp))
                    }
                }
            }
        }
    }




    // Floating "Create Set" button
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        ExtendedFloatingActionButton (
            onClick = { // Goes to screen where you can create card sets
                navController.navigate("set-creation")
            },
            icon = { Icon(Icons.Filled.Edit, "Plus") },
            text = { Text("Create Set") },
            contentColor = Color.White,
            containerColor = Color(96, 67, 168),

            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 70.dp)
        )
    }
}

// Removes flashcard sets where checkbox has value true/are marked
fun deleteFromList(context: Context, viewModel: MutableState<SharedViewModel>, checkboxStates: MutableState<MutableList<Boolean>>) {
    val flashcardList = viewModel.value.getFlashcardSets().toList()
    // Loop through each checkbox
    checkboxStates.value.forEachIndexed { i, state ->
        if (state) { // Set to true means delete
            viewModel.value.removeFlashcardSet(context, flashcardList[i].first) // Delete from viewmodel
        }
    }
}
