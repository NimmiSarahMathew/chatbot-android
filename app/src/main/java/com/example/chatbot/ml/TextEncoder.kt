package com.example.chatbot.ml

import com.example.chatbot.Constants

/**
 * Turns a sentence into the fixed-length row of 0s and 1s the model expects.
 *
 * This must behave identically to the Python training pipeline. If it diverges,
 * word positions shift and predictions break with no error raised.
 *
 * @param words  the vocabulary, in order. Position defines meaning.
 * @param lemmas surface form to lemma, e.g. "books" to "book". Exported from
 *               Python because Kotlin has no WordNet.
 */
class TextEncoder(
    private val words: List<String>,
    private val lemmas: Map<String, String>
) {

    /** Number of slots in an encoded row; must match the model's input size. */
    val vocabularySize: Int get() = words.size

    /** Lowercase, split contractions off, drop punctuation, then lemmatise. */
    fun tokenize(text: String): List<String> {
        var s = text.lowercase()
        for (c in Constants.CONTRACTIONS) s = s.replace(c, " $c ")
        return s.split(Regex("\\s+"))
            .map { token ->
                if (token in Constants.CONTRACTIONS) token
                else token.filter { it.isLetterOrDigit() }
            }
            .filter { it.isNotEmpty() }
            .map { lemmas[it] ?: it }
    }

    /** One slot per vocabulary word: 1f if present in the tokens, 0f if not. */
    fun bagOfWords(tokens: List<String>): FloatArray {
        val bag = FloatArray(words.size)
        for (i in words.indices) {
            if (words[i] in tokens) bag[i] = 1f
        }
        return bag
    }

    /** Convenience: tokenise then encode. */
    fun encode(text: String): FloatArray = bagOfWords(tokenize(text))
}
