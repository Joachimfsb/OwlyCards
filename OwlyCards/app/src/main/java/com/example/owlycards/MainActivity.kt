package com.example.owlycards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owlycards.ui.theme.OwlyCardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OwlyCardsTheme {
                OwlyApp(SharedViewModel(LocalContext.current))
            }
        }
    }
}



// Main app setup (run on creation)
@Composable
fun OwlyApp(initViewModel: SharedViewModel) {
    val viewModel = remember { mutableStateOf(initViewModel) }

    val navController = rememberNavController() // Nav controller

    // Determine start destination
    val startDest = if (viewModel.value.config.setupComplete) "cards_sets" else "welcome"

    // Define routes
    NavHost(navController = navController, startDestination = startDest) { //program starts at
                                                                             //startMenu screen
        composable("welcome") { //start menu screen
            WelcomeView(viewModel, navController)
        }
        composable("cards_sets") { //card sets screen. create and delete card sets
            FlashMenuView(navController)
        }
        composable("set-creation"){ //create a new flashcard set. add elements to new set
            SetCreationnView(navController)
        }
        composable("study-set/{flashsetName}") { backStackEntry ->
            val flashsetName = backStackEntry.arguments?.getString("flashsetName") ?: ""
            StudySetView(navController, flashsetName)
        }
        composable("quiz") { //create quizes based on card sets
            //TODO: lag fil og spill
        }
        composable("match-set/{flashsetName}") { backStackEntry ->
            val flashsetName = backStackEntry.arguments?.getString("flashsetName") ?: ""
            MatchSetView(navController, flashsetName)
        }
    }
}