package com.example.owlycards

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.File

@Preview(showBackground = true)
@Composable
fun SetCreationPreview(){

}

@Composable
fun SetCreationnView(navController: NavController, modifier: Modifier = Modifier) {
    SetCreationViewPage(navController = navController, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun SetCreationViewPage(navController: NavController, modifier: Modifier = Modifier) {
    val writtenCardSet = remember { mutableStateOf(mutableListOf<Pair<String, String>>()) }
    val checkboxStates = remember { mutableStateOf(mutableListOf<Boolean>()) } //status of checkbox
    var tmpQuestion by remember { mutableStateOf("") } //the temporary text in the text field
    var tmpAnswer by remember { mutableStateOf("") } //the temporary text in the text field
    var saveBool by remember { mutableStateOf(false) }
    var setName = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!saveBool) {
                Text(
                    text = "Creating Flashcard Set",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Row() {
                    Button(onClick = {
                        navController.navigate("cards_sets")
                    }) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    Button(onClick = {
                        deleteFromList(writtenCardSet, checkboxStates)
                    }) {
                        Text(
                            text = "Delete"
                        )
                    }
                }

                TextField( //text field to write name of items
                    value = tmpQuestion, //the temporary value
                    onValueChange = { tmpQuestion = it }, //temp changes to what the user writes
                    label = { Text("Question") }
                )
                TextField( //text field to write name of items
                    value = tmpAnswer, //the temporary value
                    onValueChange = { tmpAnswer = it }, //temp changes to what the user writes
                    label = { Text("Answer") }
                )
                Row() {
                    if (writtenCardSet.value.isNotEmpty()) {
                        Button(onClick = {
                            saveBool = !saveBool
                        }) {
                            Text(
                                text = "Save"
                            )
                        }
                    }
                    Button(onClick = { //if add button is clicked
                        // Check for empty fields
                        if (tmpQuestion.isBlank() || tmpAnswer.isBlank()) {

                        } else {
                            // Add the card to the set and reset fields
                            addToSet(tmpQuestion, tmpAnswer, writtenCardSet, checkboxStates)
                            tmpQuestion = ""
                            tmpAnswer = ""
                        }
                    }) {
                        Text(
                            text = "Add"
                        )
                    }
                }


                if (writtenCardSet.value.isNotEmpty()) { // Check if the list is NOT empty
                    LazyColumn { // LazyColumn used so the column only uses the space it needs
                        items(writtenCardSet.value) { (question, answer) -> // Iterate over each item
                            val index = writtenCardSet.value.indexOfFirst { it.first == question } // Get the index of the current question
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween // Distribute space evenly
                            ) { // Use Row for alignment
                                Text(
                                    text = "Question: \n$question",
                                    color = Color.White
                                ) // Display the question
                                Text(
                                    text = "Answer: \n$answer",
                                    color = Color.White
                                ) // Display the answer
                                Checkbox( // Item's checkbox
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.White,
                                        checkmarkColor = Color.Black, // Adjust checkmark color if needed
                                        uncheckedColor = Color.White
                                    ),
                                    checked = checkboxStates.value[index], // Get the current checkbox state
                                    onCheckedChange = { isChecked ->
                                        checkboxStates.value = // Change value when clicked
                                            checkboxStates.value.toMutableList().apply {
                                                set(index, isChecked) // Update the checkbox state
                                            }
                                    }
                                )
                            }
                        }
                    }
                } else { // If the list is empty
                    Text(
                        text = "No cards added yet...",
                        color = Color.White
                    )
                }
            } else{
                Text(
                    text = "Saving Flashcard Set",
                    color = Color.White,
                    fontSize = 30.sp
                )
                TextField(
                    value = setName.value,
                    onValueChange = {setName.value = it},
                    label = { Text("Name the set") },
                )
                Button( onClick = {
                    SaveFlashcardSet(writtenCardSet, context, setName)
                    navController.navigate("cards_sets")
                }){
                    Text(
                        text = "Save"
                    )
                }
            }
        }
    }
}

//adds a new item to the list
fun addToSet(question: String, answer: String, list: MutableState<MutableList<Pair<String, String>>>, checkboxStates: MutableState<MutableList<Boolean>>) {
    // Add question-answer pair to the map
    list.value.add(Pair(question, answer))

    // Add a new checkbox state (unchecked) to the list
    checkboxStates.value = checkboxStates.value.toMutableList().apply { add(false) }
}

// Removes items where checkbox has value true
fun deleteFromList(list: MutableState<MutableList<Pair<String, String>>>, checkboxStates: MutableState<MutableList<Boolean>>){
    // Create a new map to hold items that are not checked
    val updatedList = mutableListOf<Pair<String, String>>()
    val updatedCheckboxStates = mutableListOf<Boolean>()

    // Iterate over the entries of the map with index
    list.value.forEachIndexed { index, QnA ->
        val (q, a) = QnA
        if (index < checkboxStates.value.size && !checkboxStates.value[index]) {
            updatedList.add(QnA) // Keep the entry in the new map
            updatedCheckboxStates.add(false) // Keep checkbox as unchecked
        }
    }

    // Update the original list and checkbox states
    list.value = updatedList
    checkboxStates.value = updatedCheckboxStates
}

fun SaveFlashcardSet(QandA: MutableState<MutableList<Pair<String, String>>>, context: Context, setName: MutableState<String>) {
    // Convert Map entries to CSV format with "Country,Capital" per line
    val data = QandA.value.joinToString("\n") { (q, a) ->
        "$q¤$a"
    }

    // Save to file with the given setName
    context.openFileOutput("${setName.value}.csv", Context.MODE_PRIVATE).use {
        it.write(data.toByteArray())
    }
}
/**************************************************************************************************/
/**************************************************************************************************/
/**************************************************************************************************/
/**************************************************************************************************/
