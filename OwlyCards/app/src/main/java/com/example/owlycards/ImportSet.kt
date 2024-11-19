package com.example.owlycards

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.MutableState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owlycards.data.FlashcardSet
import java.io.File
import java.io.FileInputStream

@Preview(showBackground = true)
@Composable
fun ImportPreview(){

}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ImportView(navController: NavController, modifier: Modifier = Modifier) {
    ImportViewPage(navController, modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ImportViewPage(navController: NavController, modifier: Modifier = Modifier) {
    // Manual recompose
    var recompose by remember { mutableIntStateOf(0) }
    LaunchedEffect(recompose) { }
    val contextApp = LocalContext.current
    val flashcardSetsInDownloadsVar = flashcardSetsInDownloads(contextApp)

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopBarSmall("Import from Downloads", true, navController)
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
                itemsIndexed(flashcardSetsInDownloadsVar) { index, flashcardSet ->
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
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column { // Column containing both the name of the card set and game mode buttons
                                Text(
                                    // The name of the saved flashcard set
                                    text = flashcardSet,
                                    fontSize = 20.sp,
                                )

                                // Buttons to start the different game modes:
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    // Study button
                                    Button(
                                        modifier = Modifier.padding(end = 6.dp),
                                        onClick = {
                                            importFlashcardSet(contextApp, flashcardSet)
                                            restartApp(contextApp)
                                        },
                                    ) {
                                        Text("Import")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Restarts the app so viewmodels and lists are updated
 * */
fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    context.startActivity(intent)
    (context as? Activity)?.finish()
}

/**
 * Returns the contents of a file located in Downloads on the device
 * */
@RequiresApi(Build.VERSION_CODES.Q)
fun readFileFromDownloads(context: Context, fileName: String): String {
    val downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI // Downloads URI
    val projection = arrayOf(MediaStore.Downloads._ID, MediaStore.Downloads.DISPLAY_NAME)

    // Query for the file in Downloads by its name
    val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(fileName)

    val cursor = context.contentResolver.query(
        downloadsUri,
        projection,
        selection,
        selectionArgs,
        null
    )

    cursor?.use { // Closes cursor after use
        if (it.moveToFirst()) { //checks if there are contents in Downloads
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Downloads._ID)
            val id = it.getLong(idColumn) //fetches file with correct file id

            val fileUri = ContentUris.withAppendedId(downloadsUri, id) // Get the file's URI

            try {
                // Open the InputStream and read the file content
                context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    return inputStream.bufferedReader().use { it.readText() }
                }
            } catch (e: Exception) {
                Log.e("FileRead", "Error reading file", e)
                return ""
            }
        }
    }

    Log.e("FileRead", "File not found in Downloads")
    return "" // Returns an empty string if the file does not exist (should realistically never happen
}

/**
 * Creates a file and copies the chosen file from downloads contents into the new file
 * */
@RequiresApi(Build.VERSION_CODES.Q)
fun importFlashcardSet(context: Context, setName: String){
    var contents = readFileFromDownloads(context, setName) // "Copies" string from file in Downloads
    val flashcardSetsDir = File(context.filesDir, "flashcard_sets") // Internal app storage
    var importedSetName = setName.substring(0, setName.length - 5)
    // ^Removes ".owly"

    // Create the directory if it doesn't exist
    if (!flashcardSetsDir.exists()) {
        flashcardSetsDir.mkdirs() // Create the directory needed
    }

    var importingSet = File(flashcardSetsDir, importedSetName)
    importingSet.writeText(contents) // Writes contents into file
}

@RequiresApi(Build.VERSION_CODES.Q)
fun flashcardSetsInDownloads(context: Context): List<String> {
    val flashCardsInDownloads = mutableListOf<String>() // List containing flashcards in downloads
    val downloadsURI = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Downloads.DISPLAY_NAME)

    val selection = "${MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
    val owly = arrayOf("%.owly") // Only include files ending with .owly

    val cursor = context.contentResolver.query(
        downloadsURI,
        projection,
        selection,
        owly,
        null
    )

    cursor?.use { // Closes cursor when done
        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
        while (it.moveToNext()){
            val name = it.getString(nameColumn)
            flashCardsInDownloads.add(name)
        }
    }

    return flashCardsInDownloads
}

