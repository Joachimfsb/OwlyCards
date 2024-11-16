package com.example.owlycards.data

import android.content.Context
import java.io.File

class Config
    (private val context: Context) {
    private var _name = ""
    private var _setupComplete = false

    var name: String
        get() = _name
        set(value) { _name = value.replace(";", ","); saveState() }
    var setupComplete: Boolean
        get() = _setupComplete
        set(value) { _setupComplete = value; saveState() }


    // Constructor (Loads data (if exists) from internal storage)
    init {
        val file = File(context.filesDir, "config.csv")
        if (file.exists()) {
            try {
                val rawData = file.readText() // File content
                val dataList = rawData.split(";") // Split
                // Set data
                this._name = dataList[0]
                this._setupComplete = dataList[1] == "true"
            } catch (_: Exception) { }
        }
    }

    // Save state (data) to internal storage
    private fun saveState() {
        // Prep data
        val data =
                this._name + ";" +
                this._setupComplete.toString()

        context.openFileOutput("config.csv", Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }
}
