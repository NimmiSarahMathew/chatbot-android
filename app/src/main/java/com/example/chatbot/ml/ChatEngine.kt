package com.example.chatbot.ml

import android.content.Context
import com.example.chatbot.Constants
import org.json.JSONArray
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * Runs the library-assistant model on device.
 *
 * The .tflite file is only the classifier - its input is 263 floats, not text.
 * Turning a sentence into those floats happens here, and must match the Python
 * training pipeline exactly or positions shift and predictions break silently.
 */
class ChatEngine(context: Context) {

    data class Reply(val text: String, val tag: String, val confidence: Float)

    private val words: List<String> =
        context.readJsonArray(Constants.ASSET_WORDS)            // input positions - order is critical
    private val classes: List<String> =
        context.readJsonArray(Constants.ASSET_CLASSES)          // output positions - order is critical
    private val lemmas: Map<String, String>    // e.g. "books" -> "book"
    private val responses: Map<String, List<String>>
    private val interpreter: Interpreter
    private val encoder: TextEncoder

    init {

        val lemmaJson = JSONObject(context.readAsset(Constants.ASSET_LEMMAS))
        lemmas = lemmaJson.keys().asSequence().associateWith { lemmaJson.getString(it) }

        val intents = JSONObject(context.readAsset(Constants.ASSET_INTENTS)).getJSONArray(Constants.JSON_INTENTS)
        responses = buildMap {
            for (i in 0 until intents.length()) {
                val intent = intents.getJSONObject(i)
                val list = intent.getJSONArray(Constants.JSON_RESPONSES)
                put(intent.getString(Constants.JSON_TAG), List(list.length()) { list.getString(it) })
            }
        }

        interpreter = Interpreter(context.loadModelFile(Constants.ASSET_MODEL))
        encoder = TextEncoder(words, lemmas)
    }

    fun respond(text: String, fallbackText: String): Reply {
        val bag = encoder.encode(text)

        // Nothing recognised: the model would return its empty-input bias, which
        // looks confident but means nothing. Fall back instead.
        if (bag.none { it == 1f }) return reply(Constants.FALLBACK_TAG, 0f, fallbackText)

        val output = Array(1) { FloatArray(classes.size) }
        interpreter.run(arrayOf(bag), output)

        val probs = output[0]
        var best = 0
        for (i in probs.indices) if (probs[i] > probs[best]) best = i
        val confidence = probs[best]

        return if (confidence < Constants.CONFIDENCE_FLOOR) {
            reply(Constants.FALLBACK_TAG, confidence, fallbackText)
        } else {
            reply(classes[best], confidence, fallbackText)
        }
    }

    private fun reply(tag: String, confidence: Float, fallbackText: String): Reply =
        Reply(responses[tag]?.random() ?: fallbackText, tag, confidence)
}

// --- asset helpers ---------------------------------------------------------

private fun Context.readAsset(name: String): String =
    assets.open(name).bufferedReader().use { it.readText() }

private fun Context.readJsonArray(name: String): List<String> {
    val arr = JSONArray(readAsset(name))
    return List(arr.length()) { arr.getString(it) }
}

/** A .tflite file is memory-mapped, which is why it must not be compressed. */
private fun Context.loadModelFile(name: String): MappedByteBuffer {
    assets.openFd(name).use { afd ->
        FileInputStream(afd.fileDescriptor).use { stream ->
            return stream.channel.map(
                FileChannel.MapMode.READ_ONLY, afd.startOffset, afd.declaredLength
            )
        }
    }
}
