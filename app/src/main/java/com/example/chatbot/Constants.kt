package com.example.chatbot

/** Non-user-facing configuration. Display text lives in strings.xml. */
object Constants {

    // Asset filenames bundled in app/src/main/assets
    const val ASSET_MODEL = "chatbot.tflite"
    const val ASSET_WORDS = "words.json"
    const val ASSET_CLASSES = "classes.json"
    const val ASSET_LEMMAS = "lemmas.json"
    const val ASSET_INTENTS = "intents.json"

    // Keys inside intents.json
    const val JSON_INTENTS = "intents"
    const val JSON_TAG = "tag"
    const val JSON_RESPONSES = "responses"

    /** Below this, treat the prediction as unreliable and fall back. */
    const val CONFIDENCE_FLOOR = 0.60f

    /** Intent used when nothing is recognised or confidence is too low. */
    const val FALLBACK_TAG = "fallback"

    /**
     * NLTK splits these off as separate tokens ("don't" -> ["do", "n't"]).
     * The Kotlin tokeniser must match, or these vocabulary positions never fire.
     */
    val CONTRACTIONS = listOf("n't", "'re", "'ve", "'ll", "'m", "'s", "'d")

    const val SPEAKER_USER = "You"
    const val SPEAKER_BOT = "Assistant"
}
