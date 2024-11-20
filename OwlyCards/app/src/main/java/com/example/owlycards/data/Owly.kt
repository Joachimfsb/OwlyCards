package com.example.owlycards.data

class Owly(name: String) {

    fun greet(): String { return greetings.shuffled().first() }
    fun motivate(): String { return motivation.shuffled().first() }
    fun remind(): String { return reminders.shuffled().first() }

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
    // Keep static (don't use name attribute) to allow worker threads to use these strings
    private val reminders = listOf(
        "Time to spread your wings! Let's learn something today!",
        "Remember to study today! Remember: knowledge = Power \uD83E\uDD11",
        "Remember to set aside some time for studying today! \uD83D\uDCDA",
        "Hoo's got a test coming up? Make sure to review your cards! \uD83E\uDD89",
        "Don't let the day fly by! Take a moment to study! \uD83D\uDE04",
        "Remember, every little bit helps! Let's tackle that study material!",
        "It's study o'clock! Time to memorize! \uD83E\uDDE0",
        "Hoo's got goals? Come learn some more! \uD83C\uDF1F",
        "Just a gentle nudge: Your brain will thank you for studying today \uD83D\uDE04",
        "Don't forget what you learned last time! Consistency is key! ⏰",
        "Hoo's ready to learn? Dive into your study materials and soar to success! \uD83E\uDD47"
    )
}