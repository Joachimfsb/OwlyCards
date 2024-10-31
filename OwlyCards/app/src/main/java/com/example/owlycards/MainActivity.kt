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
    NavHost(navController = navController, startDestination = "startMenu") {
        composable("startMenu") {
            StartMenuView(navController)
        }
        /*// Recipes
        navigation(
            startDestination = "list",
            route = "recipes"
        ) {
            // Recipe list
            composable("list") {
                val viewModel = it.sharedViewModel<SharedViewModel>(navController)

                RecipeListView(viewModel, navController)
            }
            // Single recipe
            composable("recipe/{id}") {
                // Get ID from route
                val id = it.arguments?.getString("id")?.toInt() ?: 0
                val viewModel = it.sharedViewModel<SharedViewModel>(navController)

                RecipeView(id, viewModel)
            }
        }*/
    }
}