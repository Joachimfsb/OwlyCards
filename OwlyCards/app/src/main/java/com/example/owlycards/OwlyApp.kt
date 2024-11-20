package com.example.owlycards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.owlycards.data.SharedViewModel
import com.example.owlycards.managers.NotificationMgr

// Main app (run on creation)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun OwlyApp(viewModel: SharedViewModel) {

    // Create reminders (even if they already exist)
    NotificationMgr(LocalContext.current).createReminders()

    // Define routes and start view
    DefineRoutes(viewModel)
}