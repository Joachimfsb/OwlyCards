package com.example.owlycards.data

class Owly(name: String) {

    fun greet(): String { return greetings.shuffled().first() }
    fun motivate(): String { return motivation.shuffled().first() }

    private val greetings = listOf(
        "Hoo-hoo! Good day, my wise friend, $name! Ready to soar into some knowledge? \uD83E\uDD89",
        "Well, hoot there! It's a splendid time for some practicing \uD83D\uDE04",
        "Hoo's ready for some fun? I am! \uD83D\uDE04",
        "G'hoo'd to have you back, $name!",
        "Greetings, earthbound creature! Ready for some learning? \uD83E\uDDE0",
        "$name! It's a hoot to see you! Let's learn \uD83D\uDCDA",
        "Hoo's there? Ah it's my good friend $name! \uD83D\uDE0D"
    )
    private val motivation = listOf(
        "Every little bit you learn helps you grow. Keep going \uD83D\uDE80",
        "You can do this $name!",
        "I believe in you $name!!",
        "One more card! Keep going \uD83D\uDE03",
        "You have the power to learn anything! Go for it! \uD83E\uDD29",
        "Hoo-hoo! You're on your way to greatness - just keep going!! \uD83D\uDCAA",
        "Mistakes are okay! They help you learn and grow!",
        "Take it one step at a time. You'll get there $name!",
        "Curiosity is your best friend! Follow it! \uD83D\uDDFA\uFE0F",
        "Every time you learn, you're building your future! \uD83E\uDDF1",
        "Hoo-hoo! Believe in yourself, you can learn anything $name!",
        "Nothing can stop you $name!",
        "Wowzers! You are impressing me $name! \uD83D\uDE0D",
        "You're a star $name! \uD83C\uDF1F"
    )
}