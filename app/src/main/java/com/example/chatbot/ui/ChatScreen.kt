package com.example.chatbot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbot.Constants
import com.example.chatbot.R
import com.example.chatbot.ml.ChatEngine
import com.example.chatbot.ui.components.MessageBubble
import com.example.chatbot.ui.components.MessageInput
import com.example.chatbot.ui.theme.Bg
import com.example.chatbot.ui.theme.Navy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val context = LocalContext.current

    // remember{} keeps the engine across recompositions. Without it the TFLite
    // model would be reloaded on every keystroke.
    val engine = remember { ChatEngine(context) }

    val greeting = stringResource(R.string.greeting)
    val fallbackText = stringResource(R.string.no_answer)

    val messages = remember { mutableStateListOf(Message(Constants.SPEAKER_BOT, greeting)) }
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    fun send() {
        val text = input.trim()
        if (text.isEmpty()) return
        input = ""
        messages.add(Message(Constants.SPEAKER_USER, text))

        val reply = engine.respond(text, fallbackText)
        messages.add(
            Message(
                who = Constants.SPEAKER_BOT,
                body = reply.text,
                meta = "intent: ${reply.tag}  ·  confidence: ${(reply.confidence * 100).toInt()}%"
            )
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        containerColor = Bg,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.title), fontWeight = FontWeight.Bold, color = Navy)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Bg)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Bg)
                .imePadding()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message -> MessageBubble(message) }
            }

            MessageInput(value = input, onValueChange = { input = it }, onSend = { send() })
        }
    }
}
