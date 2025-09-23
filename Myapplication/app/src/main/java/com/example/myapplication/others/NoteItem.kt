package com.example.myapplication.others

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Note
import androidx.compose.foundation.combinedClickable

@Composable
fun NoteItem(
    note: Note,
    onLongPress: () -> Unit,
    onDragToTrash: () -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(
                if (note.isSelected)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(12.dp)
            )
            .pointerInput(note.id) {
                detectDragGestures(
                    onDragStart = { onLongPress() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    },
                    onDragEnd = {
                        // giả lập “thả vào trash” nếu kéo xuống dưới >150dp
                        if (offset.y > 150f) onDragToTrash()
                        offset = Offset.Zero
                    }
                )
            }
            .padding(16.dp)
    ) {
        Text(note.text, style = MaterialTheme.typography.bodyLarge)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (note.isSelected)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = note.text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}
