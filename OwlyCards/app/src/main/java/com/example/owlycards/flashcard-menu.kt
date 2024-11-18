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
import java.io.File

@Preview(showBackground = true)
@Composable
fun FlashMenuPreview(){

}

@Composable
fun FlashMenuView(navController: NavController, modifier: Modifier = Modifier) {
    FlashMenuViewPage(navController = navController, modifier = modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun FlashMenuViewPage(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val savedSets = remember { mutableStateOf(mutableListOf<String>().apply {
        addAll(PrintStoredSets(context)) // Adds all filenames to list(savedSets)
    })}

    // Initialize checkbox states to false
    val checkboxStates = remember { mutableStateOf(MutableList(savedSets.value.size) { false }) }

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

            Row() {
                Button(onClick = {
                    deleteFromList(savedSets, checkboxStates, context) // Deletes marked sets
                }) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Back-button
                Button(onClick = { navController.popBackStack() }) {
                    Text("Back") // Goes back to previous screen
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { // Goes to screeen where you can create card sets
                    navController.navigate("set-creation")
                }) {
                    Text("New Set")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (savedSets.value.isNotEmpty()) { // This is shown on screen if there are saved sets
                LazyColumn { // Lazy column so the column can expand
                    itemsIndexed(savedSets.value) { index, setName ->
                        Row( // Each set gets its own row wich fills the width of screen
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
                            ) { // Column containing both the name of the card set and gamemode buttons
                                Text( // The name of the saved flashcard set
                                    text = findName(setName),
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
                                        navController.navigate("study-set/$setName")
                                    }) {
                                        Text("Study")
                                    }

                                    // Match button
                                    Button(onClick = {
                                        navController.navigate("match-set/$setName")
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

//gets the name of all files stored in com.example.owlycards excluding the required file:
//profileInstalled which is needed for program to work correctly
//This can be used in other files as well that needs to load in the flashcard sets
fun PrintStoredSets(context: Context): List<String>{
    val directory = File(context.filesDir, "flashcard_sets") //go into the directory that stores files for owlycards
    val sets = directory.listFiles() //saves list into sets
    //returns the file/set names expect profileInstalled which is needed
    return sets?.map { it.name } ?: emptyList()
}

//removes flashcard sets where checkbox has value true/are marked
fun deleteFromList(list: MutableState<MutableList<String>>, checkboxStates: MutableState<MutableList<Boolean>>, context: Context) {
    val markedSets = mutableListOf<String>() //creates an empty list of strings
    for(set in list.value.indices){
        if(checkboxStates.value[set]){ //if a checkbox is marked, add it to markedSets list
            markedSets.add(list.value[set])
        }
    }

    for(set in markedSets){ //deletes all files in the markedSets list
        val directory = File(context.filesDir, "flashcard_sets")
        val file = File(directory, set)
        file.delete()
    }

    //put all sets that are not marked into new list
    val updatedList = list.value.filterIndexed{ index, _ -> !checkboxStates.value[index] }.toMutableList()
    val updatedCheckbox = checkboxStates.value.filterIndexed{ index, _ -> !checkboxStates.value[index]}.toMutableList()

    list.value = updatedList
    checkboxStates.value = updatedCheckbox
}

// Function to find the name a Flashcard-set (Remove filetype: .csv / .txt)
fun findName(name: String): String {
    val temp: String

    // If the name is longer than 4 letters, return a substring
    if (name.length >= 4) {
        temp = name.substring(0, name.length - 4)
    } else { // Else return the name
        temp = name
    }

    return temp
}