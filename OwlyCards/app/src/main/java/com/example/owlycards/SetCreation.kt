package com.example.owlycards

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.owlycards.data.Flashcard
import com.example.owlycards.data.FlashcardSet

@Preview(showBackground = true)
@Composable
fun SetCreationPreview(){

}

@Composable
fun SetCreationnView(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {
    SetCreationViewPage(viewModel, navController,
        modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center))
}

@Composable
fun SetCreationViewPage(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {
    val writtenCardSet = remember { mutableStateOf(mutableListOf<Flashcard>()) }
    //^where questions and answers will be stored. Need to be list and not map to avoid bug
    //where only the first element would be saved to file
    val checkboxStates = remember { mutableStateOf(mutableListOf<Boolean>()) } //status of checkbox
    var tmpQuestion by remember { mutableStateOf("") } //the temporary text in the text field
    var tmpAnswer by remember { mutableStateOf("") } //the temporary text in the text field
    var saveBool by remember { mutableStateOf(false) } //decides what's shown on screen
    val setName = remember { mutableStateOf("") }   //user gives name to flashcardset
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!saveBool) { //if the save button has not been pressed
                Text(
                    text = "Creating Flashcard Set",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Row {
                    Button(onClick = {
                        navController.navigate("cards_sets")
                    }) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    Button(onClick = {
                        deleteFromList(writtenCardSet, checkboxStates) //deletes marked sets with
                                                                       //marked checkboxes
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
                Row {
                    if (writtenCardSet.value.isNotEmpty()) { //if a Q&A has been made, button shows
                        Button(onClick = {
                            saveBool = !saveBool //changes layout/look
                        }) {
                            Text(
                                text = "Save"
                            )
                        }
                    }
                    Button(onClick = { //if add button is clicked
                        // Check for empty fields
                        if (tmpQuestion.isBlank() || tmpAnswer.isBlank()) { //if either Q or A is
                                                                            //empty
                        } else { //when both Q&A has values
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

                if (writtenCardSet.value.isNotEmpty()) { //if there are Q&As in the list
                    LazyColumn { //lazyColumn used so the column can grow
                        itemsIndexed(writtenCardSet.value) { i, flashcard -> //iterate over each set
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween // Distribute space evenly
                            ) {
                                Text( //display the question
                                    text = "Question: \n${flashcard.question}",
                                    color = Color.White
                                )
                                Text( //display the answer
                                    text = "Answer: \n${flashcard.answer}",
                                    color = Color.White
                                )
                                Checkbox( //sets checkbox. if marked Q&A can be deleted if wanted
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.White,
                                        checkmarkColor = Color.Black,
                                        uncheckedColor = Color.White
                                    ),
                                    checked = checkboxStates.value[i], //get the current checkbox state
                                    onCheckedChange = { isChecked ->
                                        checkboxStates.value = //change value when clicked
                                            checkboxStates.value.toMutableList().apply {
                                                set(i, isChecked)
                                            }
                                    }
                                )
                            }
                        }
                    }
                } else { //if there are no Q&As added yet
                    Text(
                        text = "No cards added yet...",
                        color = Color.White
                    )
                }
            } else{ //if the user had pressed save button
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
                Button( onClick = { //saved the flashcard set with the name user chose. Then goes
                                    //back to the flashcard menu
                    // Create flashcard set
                    val flashcardSet = FlashcardSet(context, setName.value)
                    val flashcardList = mutableListOf<Flashcard>()
                    writtenCardSet.value.forEach {
                        flashcardList.add(it)
                    }
                    flashcardSet.setFlashcards(flashcardList)

                    // Store in viewmodel
                    viewModel.value.addFlashcardSet(flashcardSet.name, flashcardSet)

                    // Navigate
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
fun addToSet(question: String, answer: String, list: MutableState<MutableList<Flashcard>>, checkboxStates: MutableState<MutableList<Boolean>>) {
    //adds new Q&A to list. Uses list with pair and not map to avoid bug specified earlier
    list.value.add(Flashcard(question, answer))

    //sets the new card/Q&A checkbox to false
    checkboxStates.value = checkboxStates.value.toMutableList().apply { add(false) }
}

//removes items where checkbox has value true
fun deleteFromList(list: MutableState<MutableList<Flashcard>>, checkboxStates: MutableState<MutableList<Boolean>>){
    // Create a new list to hold items that are not checked
    val updatedList = mutableListOf<Flashcard>()
    val updatedCheckboxStates = mutableListOf<Boolean>()

    //iterate over the entries of the list with index
    list.value.forEachIndexed { index, qa ->
        if (index < checkboxStates.value.size && !checkboxStates.value[index]) { //if checkbox is
                                                                                 //not marked
            updatedList.add(qa) //add card/Q&A to updated list
            updatedCheckboxStates.add(false) //add card/Q&A checkbox to updated checkbox with false
        }
    }

    list.value = updatedList //gives list updatedList's elements
    checkboxStates.value = updatedCheckboxStates //gives checkboxes updatedCheckboxStates's elements
}


