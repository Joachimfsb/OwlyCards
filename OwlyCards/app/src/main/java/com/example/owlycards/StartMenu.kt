package com.example.owlycards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun StartMenuPreview(){

}

@Composable
fun StartMenuView(navController: NavController, modifier: Modifier = Modifier) {
    StartMenuViewPage(navController = navController, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun StartMenuViewPage(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(Color.DarkGray).wrapContentSize(Alignment.Center)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp)) //moved header text downwards
            Text(
                text = "    The Owly\nFlashcard App", //header text
                fontSize = 25.sp, //text size
                modifier = Modifier.align(Alignment.CenterHorizontally), //centers the text
                color = Color.White //makes text white color
            )
            Image(
                painter = painterResource(id = R.drawable.owly_home), //image of owly
                contentDescription = "Owly Start", //description of picture
                modifier = Modifier
                    .size(500.dp) //image size
                    .align(Alignment.CenterHorizontally), //centers image
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp)) //decides how much space unerneeth image

            Button(onClick = { //if delete button is clicked
                navController.navigate("menuSelection")
            }) {
                Text(
                    text = "Start"
                )
            }
        }
    }
}