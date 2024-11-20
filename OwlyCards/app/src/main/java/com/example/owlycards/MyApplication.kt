package com.example.owlycards

import android.app.Application
import java.util.Calendar

class MyApplication : Application() {
    var inForeground = false
    var lastClosed: Calendar = Calendar.getInstance()
}