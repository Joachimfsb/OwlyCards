package com.example.owlycards

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.MutableState

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun ExportPreview(){

}

@Composable
fun ExportView(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    ExportViewPage(navController, flashcardSet, modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun ExportViewPage(navController: NavController, flashcardSet: FlashcardSet, modifier: Modifier = Modifier) {
    val contextApp = LocalContext.current
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBarSmall("Exporting '${flashcardSet.name}'", true, navController)
            Spacer(modifier = Modifier.height(300.dp))
            Box(
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
            ){
                Text(
                    text = "Exported flashcard sets will be saved in your device's download folder",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(300.dp))
            Box(){
                Button(onClick = {
                    var contents = readFile(contextApp.filesDir.toString() + "/flashcard_sets/" + flashcardSet.name)
                    saveToDownloadsUsingMediaStore(contextApp, contents, flashcardSet.name)
                    navController.navigate("cards_sets")
                }
                ){
                    Text(text = "Export")
                }
            }
        }
    }
}

fun readFile(filePath: String): String {
    val file = File(filePath)
    return file.readText()  // Reads the entire content as a single string
}

fun saveToDownloadsUsingMediaStore(context: Context, content: String, fileName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android 10 (API 29) and above, use MediaStore to save to Downloads
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName + ".owly") // Name file gets in downloads
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")  // Save to the Downloads folder
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            contentResolver.openOutputStream(it).use { outputStream ->
                outputStream?.write(content.toByteArray())
            }
        }
    } else {
        // For devices below Android 10, you can use the legacy method (write to public storage directly)
        val downloadsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsFolder, fileName)
        file.writeText(content)
    }
}