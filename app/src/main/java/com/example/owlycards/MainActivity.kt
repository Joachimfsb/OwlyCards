package com.example.owlycards

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import com.example.owlycards.data.SharedViewModel
import com.example.owlycards.ui.theme.OwlyCardsTheme
import java.util.Calendar

/*
 *  Main activity of the app
 */
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start app
        enableEdgeToEdge()
        setContent {
            OwlyCardsTheme {
                // Create a shared viewmodel with the current context and start the app
                OwlyApp(SharedViewModel(LocalContext.current))
            }
        }
    }

    // onStart updates application-relevant information
    override fun onStart() {
        super.onStart()
        (application as MainApplication).inForeground = true // Now in foreground
    }

    // onStop updates application-relevant information
    override fun onStop() {
        super.onStop()
        val app = application as MainApplication
        app.inForeground = false // Now not in foreground
        app.lastClosed = Calendar.getInstance() // Log the time app was stopped
    }
}
