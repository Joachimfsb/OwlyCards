package com.example.owlycards.data

import android.content.Context
import java.io.File

class SharedViewModel(private val context: Context) {

    // Data
    val config = Config(context)
    private val _flashcardSets = mutableMapOf<String, FlashcardSet>()
    val owly = Owly(config.name)

    // Flashcard manipulators
    fun getFlashcardSets(): MutableMap<String, FlashcardSet> { return _flashcardSets }
    fun getFlashcardSet(filename: String): FlashcardSet? { return this._flashcardSets[filename] }
    fun addFlashcardSet(filename: String): FlashcardSet? {
        val n = filename.replace("\n", "")
        if (this._flashcardSets[n] != null) return null // If the same name already exists
        // Does not exist
        this._flashcardSets[n] = FlashcardSet(context, n)
        return this._flashcardSets[n]
    }
    fun removeFlashcardSet(filename: String): Boolean {
        // Remove file
        val directory = File(context.filesDir, "flashcard_sets")
        if (directory.exists()) {
            val file = File(directory, filename)
            if (file.exists() && !file.delete()) {
                return false
            }
        }

        this._flashcardSets.remove(filename)
        return true
    }
    fun renameFlashcardSet(oldFilename: String, newFilename: String): Boolean {
        val nFilename = newFilename.replace("\n", "")
        if (this._flashcardSets[oldFilename] == null) return false // If the old name does not exists
        if (this._flashcardSets[nFilename] != null) return false // If the new name already exists
        // All good
        val data = this._flashcardSets[oldFilename]!!.export()
        if (!removeFlashcardSet(oldFilename)) return false
        val new = addFlashcardSet(nFilename) ?: return false
        return new.import(data)
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