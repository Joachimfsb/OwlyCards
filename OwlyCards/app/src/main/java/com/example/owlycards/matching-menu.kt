package com.example.owlycards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MatchingMenuView(navController: NavController, modifier: Modifier = Modifier) {
    MatchingMenuViewPage(navController = navController, modifier = Modifier.fillMaxSize().wrapContentSize(
        Alignment.Center))
}

@Composable
fun MatchingMenuViewPage(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val savedSets = remember { mutableStateOf(mutableListOf<String>().apply {
        addAll(PrintStoredSets(context)) //adds all filenames to list(savedSets)
    })
    }

    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Matching Menu",
                color = Color.White,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Back") //goes back to previous screen
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (savedSets.value.isNotEmpty()) { //this is shown on screen if there are saved sets
                LazyColumn { //lazy column so the column can expand
                    itemsIndexed(savedSets.value) { index, setName ->
                        Row( //each set gets its own row wich fills the width of screen
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable { navController.navigate("match-set/$setName") },
                        ) {
                            Text( //the name of the saved flashcard set
                                text = setName,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                modifier = Modifier.weight(2f),
                                color = Color.White
                            )
                        }
                    }
                }
            } else { //this is shown on screen if there are no saved sets
                Text(
                    text = "No flashcard sets have been created",
                    color = Color.White
                )
            }
        }
    }
}