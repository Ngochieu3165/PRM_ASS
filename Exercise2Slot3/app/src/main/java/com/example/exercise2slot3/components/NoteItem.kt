package com.example.exercise2slot3.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.exercise2slot3.data.Note
import com.example.exercise2slot3.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onLongPress: () -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: (Offset) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    val backgroundColor = when {
        note.isSelected && isDarkTheme -> NoteSelectedDark
        note.isSelected && !isDarkTheme -> NoteSelectedLight
        isDarkTheme -> NoteBackgroundDark
        else -> NoteBackgroundLight
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp) // Add horizontal margin
            .graphicsLayer {
                translationX = if (isDragging) dragOffset.x else 0f
                translationY = if (isDragging) dragOffset.y else 0f
                alpha = if (isDragging) 0.8f else 1f
                scaleX = if (isDragging) 1.05f else 1f
                scaleY = if (isDragging) 1.05f else 1f
            }
            .combinedClickable(
                onLongClick = {
                    onLongPress()
                }
            ) {}
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { _ ->
                        isDragging = true
                        dragOffset = Offset.Zero
                        onDragStart()
                    },
                    onDragEnd = {
                        // Pass the final drag offset for checking
                        onDragEnd(dragOffset)
                        isDragging = false
                        dragOffset = Offset.Zero
                    }
                ) { _, delta ->
                    dragOffset += delta
                }
            },
        shape = RoundedCornerShape(16.dp), // Increased corner radius
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragging) 12.dp else 6.dp // Enhanced elevation
        )
    ) {
        Text(
            text = note.text,
            modifier = Modifier.padding(20.dp), // Increased padding
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}