package com.example.owlycards

import android.app.Application
import java.util.Calendar

/*
 *  MainApplication class stores information about application state
 *  Mainly used for sharing data with workers
 */
class MainApplication : Application() {
    var inForeground = false
    var lastClosed: Calendar = Calendar.getInstance()
}