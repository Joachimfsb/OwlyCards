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
    val cardSet = remember { mutableStateOf(mutableListOf<String>()) } //creating an empty list
    val writtenCardSet = remember { mutableStateOf(mutableMapOf<String, String>()) }
    val checkboxStates = remember { mutableStateOf(mutableListOf<Boolean>()) } //status of checkbox
    var tmpQuestion by remember { mutableStateOf("") } //the temporary text in the text field
    var tmpAnswer by remember { mutableStateOf("") } //the temporary text in the text field
    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    deleteFromMap(writtenCardSet, checkboxStates)
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
                    items(writtenCardSet.value.toList()) { (question, answer) -> // Iterate over each item
                        val index = writtenCardSet.value.keys.indexOf(question) // Get the index of the current question
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
        }
    }
}

//adds a new item to the list
fun addToSet(question: String, answer: String, map: MutableState<MutableMap<String, String>>, checkboxStates: MutableState<MutableList<Boolean>>) {
    // Add question-answer pair to the map
    map.value[question] = answer

    // Add a new checkbox state (unchecked) to the list
    checkboxStates.value = checkboxStates.value.toMutableList().apply { add(false) }
}

// Removes items where checkbox has value true
fun deleteFromMap(map: MutableState<MutableMap<String, String>>, checkboxStates: MutableState<MutableList<Boolean>>){
    // Create a new map to hold items that are not checked
    val updatedMap = mutableMapOf<String, String>()
    val updatedCheckboxStates = mutableListOf<Boolean>()

    // Iterate over the entries of the map with index
    map.value.entries.forEachIndexed { index, entry ->
        val (key, value) = entry
        if (index < checkboxStates.value.size && !checkboxStates.value[index]) {
            updatedMap[key] = value // Keep the entry in the new map
            updatedCheckboxStates.add(false) // Keep checkbox as unchecked
        }
    }

    // Update the original list and checkbox states
    map.value = updatedMap
    checkboxStates.value = updatedCheckboxStates
}

//adds a new item to the list
fun addToSet(question: String, answer: String, map: MutableMap<String, String>) {
    // Add question-answer pair to the map
    map[question] = answer
}

// Saves the flashcard set to internal storage
fun saveCardSet(dataList: MutableList<String>, context: Context, setName: String) {

    // Parse data before storage
    val data = dataList.joinToString("¤") { it.replace("¤", "|") }

    // Save to file
    context.openFileOutput("data.csv", Context.MODE_PRIVATE).use {
        it.write(data.toByteArray())
    }
}
/**************************************************************************************************/
/**************************************************************************************************/
/**************************************************************************************************/
/**************************************************************************************************/



// Fetches the list of groceries from internal storage
fun fetchGroceries(context: Context): MutableList<String> {

    var string = ""

    // Get file content
    context.openFileInput("data.csv").bufferedReader().useLines { lines ->
        string = lines.joinToString("")
    }

    // Parse data before storage
    val data = string.split("¤")
    if (data.size == 1 && data[0] == "") return mutableListOf()
    return data.toMutableList()
}

fun LoadFlashSets(){
    val questionAnswerList = ReadFlashSetContents("src/country_capital.csv")

    for((question, answer) in questionAnswerList){
        println("Country: $question Capital: $answer")
    }

    questionAnswerList.forEach {
            (question, answer) -> println("question: $question answer: $answer  - ForEach")
    }
}

fun ReadFlashSetContents(filePath: String): List<Pair<String,String>>{
    val questionAnswerList = mutableListOf<Pair<String,String>>()
    File(filePath).forEachLine {
            line -> val(question,answer) = line.split("¤").map {it.trim()}
        questionAnswerList.add(Pair(question,answer))
    }
    return questionAnswerList
}