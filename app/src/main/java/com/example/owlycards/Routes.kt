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

/**
 * Defines view routes and starts the first view
  */
@Composable
fun DefineRoutes(viewModel: SharedViewModel) {

    // Nav controller
    val navController = rememberNavController()

    // Determine start destination:
    // If setup is completed show flashcard-sets, else who the welcome/setup screen
    val startDest = if (viewModel.config.setupComplete) "cards_sets" else "welcome"

    // Define routes
    NavHost(navController, startDest) {
        // Welcome / Setup
        composable("welcome") {
            WelcomeView(viewModel, navController)
        }
        // List of flashcard sets
        composable("cards_sets") {
            FlashcardSetMenuView(viewModel, navController)
        }
        // Study a single flashcard set
        composable("study-set/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.getFlashcardSet(filename) // Get flashcard set by filename
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                StudySetView(viewModel.owly, flashcardSet, navController) // Yes!
            }
        }
        // Quiz game for single flashcard set
        composable("quiz/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.getFlashcardSet(filename) // Get flashcard set by filename
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this quiz set does not exist!") // No!
            } else {
                QuizScreen(navController, flashcardSet) // Yes!
            }
        }
        // Match game for single flashcard set
        composable("match-set/{flashcardSetFilename}") { backStackEntry ->
            // Parse args
            val filename = backStackEntry.arguments?.getString("flashcardSetFilename") ?: ""
            val flashcardSet = viewModel.getFlashcardSet(filename) // Get flashcard set by filename
            // Does flashcard set exist?
            if (flashcardSet == null) {
                Text("Something went wrong, this study set does not exist!") // No!
            } else {
                MatchSetView(navController, flashcardSet) // Yes!
            }
        }
    }
}