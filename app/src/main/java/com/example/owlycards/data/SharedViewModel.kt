package com.example.owlycards.data

import android.content.Context
import java.io.File

/**
 * The SharedViewModel class is a class that handles all data accessible to the views.
 * It aims to provide a set and forget data structure to allow persistent storage of data
 */
class SharedViewModel(private val context: Context) {

    // Data
    val config = Config(context)
    private val _flashcardSets = mutableMapOf<String, FlashcardSet>() // Only accessible through methods
    var owly = Owly(config.name)

    // Flashcard manipulators

    /**
     * Returns a mutable map of all flashcard sets (that allows modifying of the original objects)
     */
    fun getFlashcardSets(): MutableMap<String, FlashcardSet> { return _flashcardSets }
    /**
     * Gets a single flashcard set (mutable access)
     */
    fun getFlashcardSet(filename: String): FlashcardSet? { return this._flashcardSets[filename] }
    /**
     * Adds a new flashcard set to the data structure. Remember to set the name after creation.
     *
     * Returns a reference to the newly created object or null if the flashcard set filename already exists.
     */
    fun addFlashcardSet(filename: String): FlashcardSet? {
        val n = filename.replace("\n", "")
        if (this._flashcardSets[n] != null) return null // If the same name already exists
        // Does not exist
        this._flashcardSets[n] = FlashcardSet(context, n) // Create
        return this._flashcardSets[n]
    }

    /**
     * Remove a flashcard set. Returns: success/fail
     */
    fun removeFlashcardSet(filename: String): Boolean {
        // Remove file
        val directory = File(context.filesDir, "flashcard_sets")
        if (directory.exists()) {
            val file = File(directory, filename)
            if (file.exists() && !file.delete()) { // Delete
                return false // Flashcard exists and could not be deleted, abort!

            } // If the file does not exist or the delete succeeded, continue

        } // If directory does not exist, the flashcard cannot exist

        this._flashcardSets.remove(filename) // Remove from data structure
        return true
    }

    /**
     * Rename a flashcard set. Fails if new name already exists or old name does not exist
     */
    fun renameFlashcardSet(oldFilename: String, newFilename: String): Boolean {
        val nFilename = newFilename.replace("\n", "")
        if (this._flashcardSets[oldFilename] == null) return false // If the old name does not exists
        if (this._flashcardSets[nFilename] != null) return false // If the new name already exists
        // All good
        val data = this._flashcardSets[oldFilename]!!.export() // Export old
        if (!removeFlashcardSet(oldFilename)) return false // Remove old
        val new = addFlashcardSet(nFilename) ?: return false // Add new
        return new.import(data) // Import to new
    }

    // Constructor (fetches flashcards from internal storage)
    init {
        val directory = File(context.filesDir, "flashcard_sets")

        // Loop through each file in directory and add their data to data structure
        directory.listFiles()?.forEach {
            this._flashcardSets[it.name] = FlashcardSet(context, it.name)
        }
    }


}