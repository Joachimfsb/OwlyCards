package com.example.owlycards.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/**
 * TopBarSmall component shows the top bar with a title and optionally a back button and other action buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSmall(title: String, showBackBtn: Boolean, navController: NavController, actions: @Composable (RowScope.() -> Unit) = {}) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(title) }, // Title
        navigationIcon = {
            // Back button
            if (showBackBtn) {
                IconButton(onClick = { navController.navigate("cards_sets") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go to previous page"
                    )
                }
            }
        },
        actions = actions // Optional action buttons
    )
}
