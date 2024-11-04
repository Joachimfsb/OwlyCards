package com.example.owlycards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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
                OwlyApp()
            }
        }
    }
}

// Function gets the shared viewmodel
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


// Main app setup (run on creation)
@Composable
@Preview (showBackground = true)
fun OwlyApp() {
    val navController = rememberNavController() // Nav controller

    // Define routes
    NavHost(navController = navController, startDestination = "startMenu") { //program starts at
                                                                             //startMenu screen
        composable("startMenu") { //start menu screen
            StartMenuView(navController)
        }
        composable("menuSelection") { //menu selection screen
            MenuSelectionView(navController)
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
        composable("matchmaking") { //match making games using flashcard sets
            //TODO: lag fil og spill
        }
    }
}