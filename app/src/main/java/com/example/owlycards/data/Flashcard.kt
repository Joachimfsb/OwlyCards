package com.example.owlycards.data

/**
 * The Flashcard class stores and manages flashcards
 */
class Flashcard(
    question: String,
    answer: String
) {
    // Data
    // Note that getting these variables yields their raw form, not a displayable version
    var question = question
        set(value) { field = sanitizeStrings(value) } // Settings the question automatically sanitizes it
    var answer = answer
        set(value) { field = sanitizeStrings(value) }

    // Show displayable versions
    fun getDisplayableQuestion(): String { return unsanitizeStrings(question) }
    fun getDisplayableAnswer(): String { return unsanitizeStrings(answer) }

    // Initialization
    init {
        this.question = question
        this.answer = answer
    }

    ////// PRIVATE //////

    // HELPERS
    // Sanitizes strings before storage
    private fun sanitizeStrings(s: String): String {
        return s.replace(";", "<semicolon>").replace("\n", "<newline>")
    }
    // Unsanitizes of string to for display
    private fun unsanitizeStrings(s: String): String {
        return s.replace("<semicolon>", ";").replace("<newline>", "\n")
    }


}