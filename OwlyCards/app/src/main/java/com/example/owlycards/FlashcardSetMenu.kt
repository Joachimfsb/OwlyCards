package com.example.owlycards

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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardSetMenuView(viewModel: MutableState<SharedViewModel>, navController: NavController) {

    val context = LocalContext.current

    val flashcardSets = viewModel.value.getFlashcardSets()

    var promptDeletionOfFlashcardSet by remember { mutableStateOf<String?>(null) }

    var test = ""


    // Content
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SmallAppBar("Flashcard Sets", false, navController)

                // Row to place button at the right
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Fill the width
                        .padding(top = 16.dp), // Adjust the padding to move the button further down
                    horizontalArrangement = Arrangement.End // Align the button to the right
                ) {
                    IconButton(onClick = {/*TODO*/}){
                        Icon(Icons.Filled.Edit, "Edit flashcards")
                    }
                }
            }
        }
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
                                test = DropdownMenuExample()
                                IconButton(
                                    onClick = {
                                        promptDeletionOfFlashcardSet = flashcardSet.second.name
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


    if (promptDeletionOfFlashcardSet != null) {
        AlertDialog(
            icon = { Icon(Icons.Filled.Warning, "Warning") },
            title = { Text("Confirm deletion") },
            text = {
                Text("Are you sure you want to delete the flashcard set '$promptDeletionOfFlashcardSet'?")
            },
            onDismissRequest = {
                promptDeletionOfFlashcardSet = null
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.value.removeFlashcardSet(context, promptDeletionOfFlashcardSet ?: "")
                        promptDeletionOfFlashcardSet = null
                    }
                ) {
                    Text("Yes, delete it")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        promptDeletionOfFlashcardSet = null
                    }
                ) {
                    Text("No, keep it")
                }
            }
        )
        return
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuExample(): String {
    var isExpanded by remember { mutableStateOf(false) } //the dropdown menu. if true whole menu
    var selectedOption by remember { mutableStateOf("...") } //choice starts at °C
    val options = listOf("Export", "Delete") //the options in the dropdown menu

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded //changes dropdown so you can close and open dropdown menu
        }
    ) {
        OutlinedTextField(
            value = selectedOption, //starting value of dropdown menu
            onValueChange = { },
            readOnly = true, //user cant change values
            //label = { Text("Choose") }, //text displayed at the top of dropdown menu
            modifier = Modifier.menuAnchor().width(50.dp), //the style of dropdown
        )
        DropdownMenu(
            expanded = isExpanded, //if true, menu is expanded, if false menu is not expanded
            onDismissRequest = { isExpanded = false }, //can remove the menu again
            modifier = Modifier.width(150.dp)
        ) {
            options.forEach { option -> //every list element is included in the dropdown menu
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option //selectedOption gets the value of the selected
                        //element
                        isExpanded = false
                    }
                )
            }
        }
    }
    return selectedOption //returns selected units
}