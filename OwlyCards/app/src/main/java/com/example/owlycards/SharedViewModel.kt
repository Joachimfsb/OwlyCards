package com.example.owlycards

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.owlycards.data.Config
import com.example.owlycards.data.FlashcardSet
import java.io.File

class SharedViewModel(context: Context) : ViewModel() {

    val config = Config(context)
    private val _flashcardSets = mutableMapOf<String, FlashcardSet>()

    fun getFlashcardSets(): MutableMap<String, FlashcardSet> { return _flashcardSets }
    fun getFlashcardSet(name: String): FlashcardSet? { return this._flashcardSets[name] }
    fun addFlashcardSet(name: String, set: FlashcardSet) { this._flashcardSets[name] = set }
    fun addFlashcardSet(context: Context, name: String) { this._flashcardSets[name] = FlashcardSet(context, name) }
    fun removeFlashcardSet(context: Context, name: String): Boolean {
        // Remove file
        val directory = File(context.filesDir, "flashcard_sets")
        if (directory.exists()) {
            val file = File(directory, name)
            if (file.exists() && !file.delete()) {
                return false
            }
        }

        this._flashcardSets.remove(name)
        return true
    }

    // Constructor
    init {
        // Create flashcard sets from internal storage
        val directory = File(context.filesDir, "flashcard_sets")

        // Loop through each file in directory and add their data to data structure
        directory.listFiles()?.forEach {
            this._flashcardSets[it.name] = FlashcardSet(context, it.name)
        }
    }


}