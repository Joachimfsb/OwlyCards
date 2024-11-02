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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview(showBackground = true)
@Composable
fun MenuSelectionPreview(){

}

@Composable
fun MenuSelectionView(navController: NavController, modifier: Modifier = Modifier) {
    MenuSelectionViewPage(navController = navController, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun MenuSelectionViewPage(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select an option",
                color = Color.White,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(100.dp)) //moved header text downwards
            Row( //item and checkbox on the same row
                verticalAlignment = Alignment.CenterVertically, //centers row
                modifier = Modifier
                    .fillMaxWidth() //row takes the full width
                    .padding(8.dp).clickable { //centers
                        navController.navigate("cards_sets")
                    }, //places the checkboxes all the way on the right
            ) {
                Text( //text of items
                    text = "Flashcard Sets", //item text
                    modifier = Modifier.weight(1f), //spaces out text
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.cards), //image of ramsey
                    contentDescription = "flash-cards", //description of picture
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }
            Row( //item and checkbox on the same row
                verticalAlignment = Alignment.CenterVertically, //centers row
                modifier = Modifier
                    .fillMaxWidth() //row takes the full width
                    .padding(8.dp).clickable { //centers

                    }, //places the checkboxes all the way on the right
            ) {
                Text( //text of items
                    text = "Quizes", //item text
                    modifier = Modifier.weight(1f), //spaces out text
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.quiz), //image of ramsey
                    contentDescription = "quiz", //description of picture
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }
            Row( //item and checkbox on the same row
                verticalAlignment = Alignment.CenterVertically, //centers row
                modifier = Modifier
                    .fillMaxWidth() //row takes the full width
                    .padding(8.dp).clickable { //centers

                    }, //places the checkboxes all the way on the right
            ) {
                Text( //text of items
                    text = "Match Making", //item text
                    modifier = Modifier.weight(1f), //spaces out text
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.matchmaking), //image of ramsey
                    contentDescription = "matchmaking", //description of picture
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) //decides how much space unerneeth image

            Button(onClick = { //if delete button is clicked
                navController.navigate("startMenu")
            }) {
                Text(
                    text = "Back"
                )
            }
        }
    }
}