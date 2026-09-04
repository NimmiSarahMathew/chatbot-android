package com.example.chatbot.ui

/** One line in the transcript. `meta` carries the intent/confidence debug line. */
data class Message(
    val who: String,
    val body: String,
    val meta: String? = null
)
