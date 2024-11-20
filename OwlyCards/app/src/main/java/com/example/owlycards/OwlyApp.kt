package com.example.owlycards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.owlycards.data.SharedViewModel
import com.example.owlycards.managers.NotificationMgr

// Main app setup (run on creation)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun OwlyApp(initViewModel: SharedViewModel) {
    val viewModel = remember { mutableStateOf(initViewModel) }

    val context = LocalContext.current

    // Notifications
    NotificationMgr(context).createReminders()

    DefineRoutes(viewModel)
}