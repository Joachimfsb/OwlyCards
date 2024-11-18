package com.example.owlycards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun WelcomeView(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {
    WelcomeViewMenu(viewModel, navController, modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun WelcomeViewMenu(viewModel: MutableState<SharedViewModel>, navController: NavController, modifier: Modifier = Modifier) {

    var name by remember { mutableStateOf("") }
    var nameNotFilled by remember { mutableStateOf(false)}
    var step by remember { mutableIntStateOf(1)}

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.owly), //image of Owly
                contentDescription = "Owly", //description of picture
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome to OwlyCards!", //header text
                fontSize = 28.sp, //text size
                color = Color.White //makes text white color
            )

            // Determine step
            if (step == 1) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "I am thrilled to finally meet you \uD83D\uDE04",
                    fontSize = 18.sp, //text size
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "My name is Owly and i run this place.", //header text
                    fontSize = 18.sp, //text size
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Before you begin studying, I would love to get your name.", //header text
                    fontSize = 18.sp, //text size
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
                TextField(
                    value = name,
                    onValueChange = { n ->
                        // Max length
                        if (n.length <= 30) {
                            name = n
                        }
                                    },
                    maxLines = 1,
                    label = { Text("Name") },
                    isError = nameNotFilled,
                    supportingText = {
                        if (nameNotFilled) {
                            Text("Please type your name", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // Verify that name is filled out
                    if (name.isEmpty()) {
                        // Show error
                        nameNotFilled = true
                    } else {
                    // Go to step 2
                    step = 2
                        }
                }) {
                    Text("Continue")
                }

            } else if (step == 2) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Excellent, $name! Now you are ready to use the app. Have fun \uD83D\uDE80",
                    fontSize = 18.sp, //text size
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.value.config.name = name.filter { !it.isWhitespace() }
                    viewModel.value.config.setupComplete = true
                    navController.navigate("cards_sets")
                }) {
                    Text("Begin")
                }
            }
        }
    }
}