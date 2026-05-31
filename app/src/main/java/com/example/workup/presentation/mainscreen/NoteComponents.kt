package com.example.workup.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.example.domain.entity.Note

@Composable
fun NotesListSection(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onNoteUpdated: (String, String) -> Unit // Передаем ID заметки и новый текст
) {
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        notes.forEach { note ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = note.content,
                    onValueChange = { newText ->
                        onNoteUpdated(note.id, newText)
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (note.content.isEmpty()) {
                            Text(
                                text = "Write your note here...",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .clickable { onAddNote() }
                .padding(horizontal = 24.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}