package com.example.chatbot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatbot.ui.Message
import com.example.chatbot.ui.theme.Muted
import com.example.chatbot.ui.theme.Navy

/** A single transcript entry: speaker, body, and the optional debug line. */
@Composable
fun MessageBubble(message: Message) {
    Column {
        Text(message.who, color = Navy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(Modifier.height(2.dp))
        Text(message.body, color = Navy, fontSize = 15.sp)
        message.meta?.let { meta ->
            Spacer(Modifier.height(2.dp))
            Text(meta, color = Muted, fontSize = 11.sp)
        }
    }
}
