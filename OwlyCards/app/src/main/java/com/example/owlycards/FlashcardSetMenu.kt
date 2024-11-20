package com.example.owlycards

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.components.DropdownMenuArrow
import com.example.owlycards.components.TopBarSmall
import com.example.owlycards.data.FlashcardSet
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter


@Composable
fun FlashcardSetMenuView(viewModel: MutableState<SharedViewModel>, navController: NavController) {

    val context = LocalContext.current

    val flashcardSets = viewModel.value.getFlashcardSets()

    // Prompts
    var promptDeletionOfFlashcardSet by remember { mutableStateOf<Pair<String, FlashcardSet>?>(null) }
    var promptCreateFlashcardSet by remember { mutableStateOf(false) }
    var promptErrorDialog by remember { mutableStateOf<String?>(null) }

    // Manual recompose
    var recompose by remember { mutableIntStateOf(0) }
    LaunchedEffect(recompose) { }


    // Content
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Prep import option
                val importFile = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri ->
                        uri?.let {
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val reader = BufferedReader(InputStreamReader(inputStream))
                            val rawData = reader.use { it.readText() }

                            // Create new flashcard
                            var success = false
                            val tempFilename = "temporary"

                            val flashcardSet = viewModel.value.addFlashcardSet(context, tempFilename)
                            if (flashcardSet != null) {
                                if (flashcardSet.import(rawData)) {

                                    // Use name of flashcard for filename
                                    if (viewModel.value.renameFlashcardSet(context, tempFilename, "${flashcardSet.name}.owly")) {
                                        success = true
                                        recompose++
                                    } else {
                                        promptErrorDialog = "A flashcard with the same name already exists."
                                    }
                                } else {
                                    promptErrorDialog = "Could not import. File is in an incorrect format."
                                }
                            } else {
                                promptErrorDialog = "Could not import. Something went wrong."
                            }

                            if (!success) {
                                viewModel.value.removeFlashcardSet(context, tempFilename)
                            }
                        }
                    }
                )

                TopBarSmall("Flashcard Sets", false, navController) {
                    IconButton(onClick = {
                        importFile.launch("application/octet-stream") // Import
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            "Import file",
                            Modifier.graphicsLayer { rotationZ = 90f }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                itemsIndexed(flashcardSets.toList()) { index, flashcardSet ->
                    Surface( // Each set gets its own row which fills the width of screen
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Column { // Column containing both the name of the card set and game mode buttons
                                Text(
                                    // The name of the saved flashcard set
                                    text = flashcardSet.second.name,
                                    fontSize = 20.sp,
                                )
                                Text(
                                    // The name of the saved flashcard set
                                    text = "Number of flashcards: ${flashcardSet.second.getFlashcards().size}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                )

                                // Buttons to start the different game modes:
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    // Study button
                                    Button(
                                        modifier = Modifier.padding(end = 6.dp),
                                        onClick = {
                                            navController.navigate("study-set/${flashcardSet.first}")
                                        },
                                    ) {
                                        Text("STUDY")
                                    }

                                    // Match button
                                    Button(
                                        modifier = Modifier.padding(end = 6.dp),
                                        onClick = {
                                            navController.navigate("match-set/${flashcardSet.first}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        border = BorderStroke(
                                            2.dp,
                                            ButtonDefaults.buttonColors().containerColor
                                        )
                                    ) {
                                        Text(
                                            "MATCH",
                                            color = ButtonDefaults.buttonColors().containerColor
                                        )
                                    }

                                    // Quiz button
                                    Button(
                                        onClick = {
                                            navController.navigate("quiz/${flashcardSet.first}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        border = BorderStroke(
                                            2.dp,
                                            ButtonDefaults.buttonColors().containerColor
                                        )
                                    ) {
                                        Text(
                                            "QUIZ",
                                            color = ButtonDefaults.buttonColors().containerColor
                                        )
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                // Prep download option
                                val fileSaveLauncher = rememberLauncherForActivityResult(
                                    contract = CreateDocument("application/octet-stream"),
                                    onResult = { uri ->
                                        uri?.let {
                                            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                                                OutputStreamWriter(outputStream).use { writer ->
                                                    writer.write(flashcardSet.second.export())
                                                }
                                            }
                                        }
                                    }
                                )


                                // Choice between exporting set or deleting it
                                DropdownMenuArrow (
                                    options = listOf("Share", "Export", "Delete"),
                                    onOptionSelected = {
                                        if (it == "Share") {
                                            // Get file uri
                                            val fileUri = flashcardSet.second.getFileUri()

                                            // Open share screen
                                            if (fileUri != null) {
                                                try {
                                                    val shareIntent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(
                                                            Intent.EXTRA_STREAM,
                                                            fileUri
                                                            )
                                                        type = "application/octet-stream"
                                                    }
                                                    context.startActivity(
                                                        Intent.createChooser(
                                                            shareIntent,
                                                            "Share flashcard set '${flashcardSet.second.name}'"
                                                        )
                                                    )
                                                } finally {
                                                }
                                            }
                                        } else if (it == "Export") {
                                            // Download
                                            fileSaveLauncher.launch(flashcardSet.first)
                                        } else if (it == "Delete") {
                                            promptDeletionOfFlashcardSet = flashcardSet
                                        }
                                    },
                                )
                            }
                        }
                        if (index == flashcardSets.size - 1) {
                            Spacer(Modifier.height(85.dp))
                        }
                    }
                }
            }
        }


        // Floating "Create Set" button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            ExtendedFloatingActionButton(
                onClick = { // Goes to screen where you can create card sets
                    promptCreateFlashcardSet = true
                },
                icon = { Icon(Icons.Filled.Edit, "Plus") },
                text = { Text("Create Set") },
                contentColor = Color.White,
                containerColor = Color(96, 67, 168),

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp)
            )
        }


        if (promptDeletionOfFlashcardSet != null) {
            AlertDialog(
                icon = { Icon(Icons.Filled.Warning, "Warning") },
                title = { Text("Confirm deletion") },
                text = {
                    Text("Are you sure you want to delete the flashcard set '${promptDeletionOfFlashcardSet!!.second.name}'?")
                },
                onDismissRequest = {
                    promptDeletionOfFlashcardSet = null
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.value.removeFlashcardSet(
                                context,
                                promptDeletionOfFlashcardSet!!.first
                            )
                            promptDeletionOfFlashcardSet = null
                        }
                    ) {
                        Text("Yes, delete it")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            promptDeletionOfFlashcardSet = null
                        }
                    ) {
                        Text("No, keep it")
                    }
                }
            )
        } else if (promptCreateFlashcardSet) {

            var name by remember { mutableStateOf("") }
            var nameNotFilled by remember { mutableStateOf(false) }

            AlertDialog(
                title = { Text("Create Flashcard Set") },
                text = {
                    TextField(
                        value = name.replace("\n", ""),
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
                                Text(
                                    "Please supply a name for the Flashcard Set",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                },
                onDismissRequest = {
                    promptCreateFlashcardSet = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Verify
                            val strippedName = name.filter { !it.isWhitespace() }
                            if (strippedName.isNotEmpty()) {
                                // Create flashcard
                                val flashcardSet = viewModel.value.addFlashcardSet(context, "$strippedName.owly")
                                if (flashcardSet != null) {
                                    flashcardSet.name = strippedName
                                    navController.navigate("study-set/$strippedName.owly")
                                } else {
                                    promptErrorDialog = "Could not create flashcard set: invalid or existing name"
                                }
                                promptCreateFlashcardSet = false
                            } else {
                                nameNotFilled = true
                            }
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            promptCreateFlashcardSet = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        } else if (promptErrorDialog != null) {
            AlertDialog(
                icon = { Icon(Icons.Filled.Warning, "Warning") },
                title = { Text("Something went wrong!") },
                text = {
                    Text(promptErrorDialog!!)
                },
                onDismissRequest = {
                    promptErrorDialog = null
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            promptErrorDialog = null
                        }
                    ) {
                        Text("Okay")
                    }
                },
            )
        }
    }
}

