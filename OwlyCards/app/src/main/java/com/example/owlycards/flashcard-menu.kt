package com.example.owlycards

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview(showBackground = true)
@Composable
fun FlashMenuPreview(){

}

@Composable
fun FlashMenuView(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {
    FlashMenuViewPage(viewModel, navController, modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun FlashMenuViewPage(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val flashcardSets = viewModel.value.getFlashcardSets()

    // Initialize checkbox states to false
    val checkboxStates = remember { mutableStateOf(MutableList(flashcardSets.size) { false }) }

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "FlashCardMenu",
                color = Color.White,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(30.dp))

            Row {
                Button(onClick = {
                    deleteFromList(context, viewModel, checkboxStates) // Deletes marked sets
                }) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { // Goes to screeen where you can create card sets
                    navController.navigate("set-creation")
                }) {
                    Text("New Set")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (flashcardSets.isNotEmpty()) { // This is shown on screen if there are saved sets
                LazyColumn { // Lazy column so the column can expand
                    itemsIndexed(flashcardSets.toList()) { index, flashcardSet ->
                        Row( // Each set gets its own row which fills the width of screen
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .background(
                                    Color.LightGray,
                                    shape = RoundedCornerShape(16.dp)
                                ) // Give the item a rounded light gray background
                        ) {
                            Column(
                                modifier = Modifier.height(100.dp).width(300.dp).padding(10.dp)
                            ) { // Column containing both the name of the card set and game mode buttons
                                Text( // The name of the saved flashcard set
                                    text = flashcardSet.second.name,
                                    fontSize = 20.sp,
                                    modifier = Modifier.weight(2f).fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                // Buttons to start the different gamemodes:
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Study button
                                    Button(onClick = {
                                        navController.navigate("study-set/${flashcardSet.second.name}")
                                    }) {
                                        Text("Study")
                                    }

                                    // Match button
                                    Button(onClick = {
                                        navController.navigate("match-set/${flashcardSet.second.name}")
                                    }) {
                                        Text("Match")
                                    }

                                    // Quiz button
                                    Button(onClick = {
                                        //navController.navigate("quiz-set/$setName")
                                    }) {
                                        Text("Quiz")
                                    }
                                }
                            }

                            Checkbox( // The checkbox state of card set. can be marked for deletion
                                checked = checkboxStates.value[index],
                                onCheckedChange = { isChecked ->
                                    checkboxStates.value = checkboxStates.value.toMutableList().apply {
                                        set(index, isChecked)
                                    }
                                },
                                colors = CheckboxDefaults.colors( // Sets border and square to white
                                                                  // checkmark color is black
                                    checkedColor = Color.Black,
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color.Black
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else { // This is shown on screen if there are no saved sets
                Text(
                    text = "No flashcard sets have been created",
                    color = Color.White
                )
            }
        }
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
