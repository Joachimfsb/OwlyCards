package com.example.owlycards.data

class Flashcard(
    question: String,
    answer: String
) {
    var question = question
        set(value) { field = sanitizeStrings(value) } // Settings the question automatically sanitizes it
    var answer = answer
        set(value) { field = sanitizeStrings(value) }

    // Show displayable versions
    fun getDisplayableQuestion(): String { return unsanitizeStrings(question) }
    fun getDisplayableAnswer(): String { return unsanitizeStrings(answer) }


    // HELPERS
    // Sanitizes strings before storage
    private fun sanitizeStrings(s: String): String {
        return s.replace(";", "<semicolon>").replace("\n", "<newline>")
    }
    // Unsanitizes of string to for display
    private fun unsanitizeStrings(s: String): String {
        return s.replace("<semicolon>", ";").replace("<newline>", "\n")
    }

    // Initialization
    init {
        this.question = question
        this.answer = answer
    }
}