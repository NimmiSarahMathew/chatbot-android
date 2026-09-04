package com.example.chatbot.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatbot.R
import com.example.chatbot.ui.theme.Card
import com.example.chatbot.ui.theme.Muted
import com.example.chatbot.ui.theme.Navy
import com.example.chatbot.ui.theme.Teal

/** Text field plus Send button. Stateless - the caller owns the text. */
@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.hint_message), color = Muted) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Card,
                unfocusedContainerColor = Card,
                focusedBorderColor = Teal,
                unfocusedBorderColor = Muted,
                focusedTextColor = Navy,
                unfocusedTextColor = Navy
            )
        )
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onSend,
            colors = ButtonDefaults.buttonColors(containerColor = Teal),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(stringResource(R.string.send), color = Color.White)
        }
    }
}
