package com.example.workup.presentation.splashscreen

object QuoteProvider {
    private val quotes = listOf(
        "Push yourself, because no one else is going to do it for you.",
        "Success starts with self-discipline.",
        "The only bad workout is the one that didn't happen.",
        "Sore today, strong tomorrow.",
        "Wake up with determination. Go to bed with satisfaction.",
        "Always forward.",
        "Not to the bottom, only onto the straight line."
    )

    fun getRandomQuote(): String = quotes.random()
}