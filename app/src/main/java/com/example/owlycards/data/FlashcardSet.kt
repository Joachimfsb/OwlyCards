package com.example.owlycards.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Manages and stores flashcard sets persistently
 */
class FlashcardSet(private val context: Context, private val filename: String) {
    // Internal data attributes

    private var _name = "" // Name of flashcard set
    private var _flashcards = mutableListOf<Flashcard>() // Map of flashcards

    // Public attributes with getters and setters

    // Attribute: name
    var name: String
        get() = _name
        set(value) { _name = value.replace(";", ","); saveState() }

    // Attribute: flashcards
    /**
     * Returns a copy of all flashcards.
     */
    fun getFlashcards(): MutableList<Flashcard> {return _flashcards.toMutableList() } // Returns copy
    /**
     * Adds a new flashcard.
     */
    fun addFlashcard(f: Flashcard) { _flashcards.add(f); saveState() }
    /**
     * Removes a flashcard at the given index. Returns whether the operation was successful or not.
     */
    fun removeFlashcard(index: Int): Boolean {
        // List is not empty and index is within range
        if (_flashcards.size > 0 && index < _flashcards.size && index >= 0) {
            _flashcards.removeAt(index)
            saveState()
            return true // Success
        } else {
            return false // Fail
        }
    }



    // Constructor (Loads data (if exists) from internal storage)
    init {
        // Get storage dir
        val directory = getStorageDirectory()
        if (directory != null) {
            // Check if flashcard set exists
            val file = File(directory, filename)
            if (file.exists()) {
                import(file.readText()) // Import it
            } else {
                saveState() // Flashcard set does not exist, means a new one is being created, so we should save state
            }
        }
    }

    /**
     * Gets the flashcard set's storage file uri. Could be used for exporting.
     * Returns null on error.
     */
    fun getFileUri(): Uri? {
        // Get storage dir
        val directory = getStorageDirectory()
        if (directory != null) {
            // Get file
            val file = File(directory, filename)
            if (file.exists() && file.canRead()) {
                // Get file uri
                return try {
                    FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                } catch (e: Exception) {
                    null // error
                }
            }
        }
        return null
    }


    /////////// IMPORT / EXPORT //////////////
    // File format:
    // name
    // question1;answer1
    // question2;answer2
    // ...

    /**
     * Import raw file data directly to the attributes.
     * Returns false if the operation was unsuccessful (ex. if file format is incorrect)
     */
    fun import(rawData: String): Boolean {
        try {
            // Split data into lines
            val lines = rawData.split("\n") // Split

            // Set data
            val name = lines[0] // First line is name

            val flashcards = mutableListOf<Flashcard>()
            // Loop through each line after
            lines.drop(1).forEach {
                val line = it.split(";") // Split
                flashcards.add(Flashcard(line[0], line[1]))
            }

            // Set internal data structure now that import was successful
            this._name = name
            this._flashcards = flashcards
            saveState() // Save to disk
            return true
        } catch (_: Exception) {
            return false // Something threw an exception, return false
        }
    }

    /**
     * Exports the stored data to raw file data as a string.
     */
    fun export(): String {
        val lines = mutableListOf(name) // First line is name
        this._flashcards.forEach { // Each line after is flashcards
            lines.add(it.question + ";" + it.answer)
        }
        return lines.joinToString("\n")
    }

    //////// PRIVATE METHODS ////////

    /**
     * Save state (data) to internal storage
      */
    private fun saveState() {
        // Prep data
        val data = export()

        val directory = getStorageDirectory()
        if (directory != null) {
            // Create file
            val file = File(directory, filename)
            if (file.canWrite() || file.createNewFile()) {
                file.writeText(data)
            }
        }
    }

    /**
     * Helper: Gets the storage directory for the current context
     */
    private fun getStorageDirectory(): File? {
        // Locate directory that stores flashcard-sets
        val directory = File(context.filesDir, "flashcard_sets")
        // Create dir if it does not exist
        return if (directory.exists() || directory.mkdir()) {
            directory
        } else {
            null
        }
    }

}
