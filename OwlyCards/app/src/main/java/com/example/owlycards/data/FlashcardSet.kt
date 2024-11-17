package com.example.owlycards.data

import android.content.Context
import java.io.File

class Flashcard(
    question: String,
    answer: String
) {
    var question = question
        set(value) { field = sanitizeStrings(value) }
    var answer = answer
        set(value) { field = sanitizeStrings(value) }

    private fun sanitizeStrings(s: String): String {
        return s.replace(";", ",").replace("\n", "")
    }

    init {
        this.question = question
        this.answer = answer
    }
}

class FlashcardSet
    (private val context: Context, name: String) {
    private var _name = ""
    private var _flashcards = mutableListOf<Flashcard>()

    var name: String
        get() = _name
        set(value) { _name = value.replace(";", ","); saveState() }

    fun getFlashcards(): MutableList<Flashcard> { return _flashcards }
    fun setFlashcards(flashcardList: MutableList<Flashcard>) { _flashcards = flashcardList; saveState() }
    fun addFlashcard(f: Flashcard) { _flashcards.add(f); saveState() }

    // Constructor (Loads data (if exists) from internal storage)
    init {
        this._name = name

        // Locate directory that stores flashcard-sets
        val directory = File(context.filesDir, "flashcard_sets")
        // Check if the dir exists
        if (directory.exists()) {
            // Check if flashcard set exists
            val file = File(directory, name)
            if (file.exists()) {
                try {
                    val rawData = file.readText() // File content
                    val lines = rawData.split("\n") // Split

                    // Set data
                    lines.forEach {
                        val line = it.split(";")
                        this._flashcards.add(Flashcard(line[0], line[1]))
                    }
                } catch (_: Exception) { }
            }
        }
    }

    // Save state (data) to internal storage
    private fun saveState() {
        // Prep data
        val lines = mutableListOf<String>()
        this._flashcards.forEach {
            lines.add(it.question + ";" + it.answer)
        }
        val data = lines.joinToString("\n")

        // Locate directory that stores flashcard-sets
        val directory = File(context.filesDir, "flashcard_sets")
        // Create dir if it does not exist
        if (directory.exists() || directory.mkdir()) {
            // Create file
            val file = File(directory, name)
            if (file.canWrite() || file.createNewFile()) {
                file.writeText(data)
            }
        }
    }
}
