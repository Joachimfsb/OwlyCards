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

    override fun onStart() {
        super.onStart()
        (application as MyApplication).inForeground = true
    }

    override fun onStop() {
        super.onStop()
        val app = application as MyApplication
        app.inForeground = false
        app.lastClosed = Calendar.getInstance()
    }
}
