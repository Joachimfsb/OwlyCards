package com.example.owlycards

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owlycards.ui.theme.OwlyCardsTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start app
        enableEdgeToEdge()
        setContent {
            OwlyCardsTheme {
                OwlyApp(SharedViewModel(LocalContext.current))
            }
        }
    }
}



// Main app setup (run on creation)
@RequiresApi(Build.VERSION_CODES.Q)
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
            FlashcardSetMenuView(viewModel, navController)
        }
        composable("study-set/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.value.getFlashcardSet(filename)
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                StudySetView(viewModel.value.owly, flashcardSet, navController) // Yes!
            }
        }
        composable("quiz/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.value.getFlashcardSet(filename)
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this quiz set does not exist!") // No!
            } else {
                QuizScreen(navController, flashcardSet) // Yes!
            }
        }
        composable("match-set/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.value.getFlashcardSet(filename)
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                MatchSetView(navController, flashcardSet) // Yes!
            }
        }
    }
}