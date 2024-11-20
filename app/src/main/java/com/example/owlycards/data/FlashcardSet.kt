package com.example.owlycards.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


class FlashcardSet
    (private val context: Context, val filename: String) {
    private var _name = ""
    private var _flashcards = mutableListOf<Flashcard>()

    // Public attributes with getters and setters
    // Attribute: name
    var name: String
        get() = _name
        set(value) { _name = value.replace(";", ","); saveState() }
    // Attribute: flashcards
    fun getFlashcards(): MutableList<Flashcard> { return _flashcards.toMutableList() } // Returns copy
    fun addFlashcard(f: Flashcard) { _flashcards.add(f); saveState() }
    fun removeFlashcard(index: Int): Boolean {
        // List is not empty and index is within range
        if (_flashcards.size > 0 && index < _flashcards.size && index >= 0) {
            _flashcards.removeAt(index)
            saveState()
            return true
        } else {
            return false
        }
    }

    // Constructor (Loads data (if exists) from internal storage)
    init {
        val directory = getStorageDirectory()
        if (directory != null) {
            // Check if flashcard set exists
            val file = File(directory, filename)
            if (file.exists()) {
                import(file.readText())
            } else {
                saveState() // Flashcard set does not exist, mean a new one is being created
            }
        }
    }

    fun getFileUri(): Uri? {
        // Get storage dir
        val directory = getStorageDirectory()
        if (directory != null) {
            // Get file
            val file = File(directory, filename)
            if (file.exists() && file.canRead()) {
                // Get file uri
                return try {
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                        )
                } catch (e: Exception) {
                    null
                }
            }
        }
        return null
    }

    fun import(rawData: String): Boolean {
        try {
            val lines = rawData.split("\n") // Split

            // Set data
            val name = lines[0]
            val flashcards = mutableListOf<Flashcard>()
            lines.drop(1).forEach {
                val line = it.split(";")
                flashcards.add(Flashcard(line[0], line[1]))
            }
            this._name = name
            this._flashcards = flashcards
            saveState()
            return true
        } catch (_: Exception) {
            return false
        }
    }

    fun export(): String {
        val lines = mutableListOf(name)
        this._flashcards.forEach {
            lines.add(it.question + ";" + it.answer)
        }
        return lines.joinToString("\n")
    }

    //////// PRIVATE METHODS ////////

    // Save state (data) to internal storage
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
