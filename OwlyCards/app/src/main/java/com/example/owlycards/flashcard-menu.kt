package com.example.owlycards

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Collections.addAll

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
    val context = LocalContext.current
    val savedSets = remember { mutableStateOf(mutableListOf<String>().apply {
        addAll(PrintStoredSets(context)) //adds all filenames to list
    })}
    val checkboxStates = remember { mutableStateOf(MutableList(savedSets.value.size) { false }) } // Initialize checkbox states

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
            Spacer(modifier = Modifier.height(100.dp))

            Row() {
                Button(onClick = {
                    deleteFromList(savedSets, checkboxStates, context) // Pass savedSets to delete function
                }) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { navController.navigate("menuSelection") }) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    navController.navigate("set-creation")
                }) {
                    Text("New Set")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (savedSets.value.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(savedSets.value) { index, setName ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            Text(
                                text = setName,
                                modifier = Modifier.weight(1f),
                                color = Color.White
                            )
                            Checkbox(
                                checked = checkboxStates.value[index],
                                onCheckedChange = { isChecked ->
                                    checkboxStates.value = checkboxStates.value.toMutableList().apply {
                                        set(index, isChecked)
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.White,
                                    checkmarkColor = Color.Black,
                                    uncheckedColor = Color.White
                                )
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No flashcard sets have been created",
                    color = Color.White
                )
            }
        }
    }
}

fun PrintStoredSets(context: Context): List<String>{
    val directory = context.filesDir //go into the directory
    val sets = directory.listFiles() //saves list into sets
    //returns the file/set names expect profileInstalled which is needed
    return sets?.filter { it.name != "profileInstalled" }?.map { it.name } ?: emptyList()
}

//removes flashcard sets where checkbox has value true
fun deleteFromList(list: MutableState<MutableList<String>>, checkboxStates: MutableState<MutableList<Boolean>>, context: Context) {
    val markedSets = mutableListOf<String>()
    for(set in list.value.indices){
        if(checkboxStates.value[set]){ //if a checkbox is marked, add it to list
            markedSets.add(list.value[set])
        }
    }

    for(set in markedSets){ //deletes all files in the list
        context.deleteFile((set))
    }

    //put all sets that are not marked into new list
    val updatedList = list.value.filterIndexed{ index, _ -> !checkboxStates.value[index] }.toMutableList()
    val updatedCheckbox = checkboxStates.value.filterIndexed{ index, _ -> !checkboxStates.value[index]}.toMutableList()

    list.value = updatedList
    checkboxStates.value = updatedCheckbox
}