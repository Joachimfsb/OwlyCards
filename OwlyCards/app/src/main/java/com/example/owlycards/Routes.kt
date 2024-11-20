package com.example.owlycards

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owlycards.data.SharedViewModel
import com.example.owlycards.views.FlashcardSetMenuView
import com.example.owlycards.views.MatchSetView
import com.example.owlycards.views.QuizScreen
import com.example.owlycards.views.StudySetView
import com.example.owlycards.views.WelcomeView

@Composable
fun DefineRoutes(viewModel: SharedViewModel) {

    // Nav controller
    val navController = rememberNavController()

    // Determine start destination
    val startDest = if (viewModel.config.setupComplete) "cards_sets" else "welcome"

    // Define routes
    NavHost(navController, startDest) { //program starts at
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
            val flashcardSet = viewModel.getFlashcardSet(filename)
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                StudySetView(viewModel.owly, flashcardSet, navController) // Yes!
            }
        }
        composable("quiz/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.getFlashcardSet(filename)
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
            val flashcardSet = viewModel.getFlashcardSet(filename)
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                MatchSetView(navController, flashcardSet) // Yes!
            }
        }
    }
}