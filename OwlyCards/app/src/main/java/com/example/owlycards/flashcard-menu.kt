package com.example.owlycards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview(showBackground = true)
@Composable
fun FlashMenuPreview(){

}

@Composable
fun FlashMenuView(navController: NavController, modifier: Modifier = Modifier) {
    FlashMenuViewPage(navController = navController, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun FlashMenuViewPage(navController: NavController, modifier: Modifier = Modifier) {
    val list = remember { mutableStateOf(mutableListOf<String>()) } //creating an empty list
    val checkboxStates = remember { mutableStateOf(mutableListOf<Boolean>()) } //status of checkbox

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "FlashCardMenu",
                color = Color.White,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(100.dp)) //moved header text downwards
            Row() {
                Button(onClick = {
                    deleteFromList(list, checkboxStates)
                }) {
                    Text(
                        text = "Delete"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { //if delete button is clicked
                    navController.navigate("menuSelection")
                }) {
                    Text(
                        text = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    navController.navigate("set-creation")
                }) {
                    Text(
                        text = "New Set"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(!list.value.isEmpty()){ //if the list is NOT empty
                LazyColumn { //lazyColumn used so the column only uses the space it needs
                    itemsIndexed(list.value) { index, set -> //indexes the list
                        Row( //item and checkbox on the same row
                            verticalAlignment = Alignment.CenterVertically, //centers row
                            modifier = Modifier
                                .fillMaxWidth() //row takes the full width
                                .padding(8.dp), //places the checkboxes all the way on the right
                        ) {
                            Text( //text of items
                                text = set, //item text
                                modifier = Modifier.weight(1f), //spaces out text
                                color = Color.White
                            )
                            Checkbox( //items checkbox
                                checked = checkboxStates.value[index], //sets the value of checkbox
                                onCheckedChange = { isChecked ->
                                    checkboxStates.value = //changes value when clicked
                                        checkboxStates.value.toMutableList().apply {
                                            set(index, isChecked)
                                        }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.White,
                                    checkmarkColor = Color.Black, // Adjust checkmark color if needed
                                    uncheckedColor = Color.White
                                )
                            )
                        }

                    }
                }
            }
            else{ //if the list is empty
                Text(
                    text = "No flashcard sets have been created",
                    color = Color.White
                )
            }
        }
    }
}

fun addToList(item: String, list: MutableState<MutableList<String>>, checkboxStates: MutableState<MutableList<Boolean>>) {
    list.value = list.value.toMutableList().apply { add(item) } //add element to list, value as item
    checkboxStates.value = checkboxStates.value.toMutableList().apply { add(false) }//check to false
}

//removes items where checkbox has value true
fun deleteFromList(list: MutableState<MutableList<String>>, checkboxStates: MutableState<MutableList<Boolean>>) {
    var updatedList = list.value.toMutableList() //updatedList gets same elements as list.
    var updatedCheckbox = checkboxStates.value.toMutableList() //updatedCheck gets values of
    //checkboxStates

    for (item in list.value.size - 1 downTo 0) { //for loop starts at last item and moves to 0
        if (checkboxStates.value[item]) { //if items checkBox is true
            updatedList.removeAt(item) //remove item from updatedList
            updatedCheckbox.removeAt(item) //remove item from updatedCheckbox
        }
    }

    list.value = updatedList //list gets overwritten with updatedList's elements
    checkboxStates.value = updatedCheckbox //checkboxStates gets overwritten
    // with updatedCheckbox's elements
}