package com.example.owlycards.data

import android.content.Context
import java.io.File

/**
 * The Config class manages the app's configuration persistently
 */
class Config(private val context: Context) {
    // Internal attributes
    private var _name = ""
    private var _setupComplete = false

    // Publicly facing dummy attributes that modifies the internal ones
    var name: String // The name of the user
        get() = _name
        set(value) {
            _name = value.replace(";", ",") // Remove semicolon
            saveState()
        }
    var setupComplete: Boolean // Is setup completed?
        get() = _setupComplete
        set(value) {
            _setupComplete = value
            saveState()
        }


    // Constructor (Loads data, if it exists, from internal storage)
    init {
        // Get file and check if it exists
        val file = File(context.filesDir, "config.csv")
        if (file.exists()) {
            try {
                // Read data
                val rawData = file.readText() // File content
                val dataList = rawData.split(";") // Split
                // Set data
                this._name = dataList[0]
                this._setupComplete = dataList[1] == "true"
            } catch (_: Exception) { }
        }
        // Does not exist? Use default
    }

    // Save state (data) to internal storage
    private fun saveState() {
        // Prep data
        val data =
                this._name + ";" +
                this._setupComplete.toString()

        // Write to file
        context.openFileOutput("config.csv", Context.MODE_PRIVATE).use {
            it.write(data.toByteArray())
        }
    }
}
