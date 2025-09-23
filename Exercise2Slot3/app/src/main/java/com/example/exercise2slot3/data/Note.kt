package com.example.exercise2slot3.data

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isSelected: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class DragInfo(
    val isDragging: Boolean = false,
    val draggedNote: Note? = null,
    val dragOffset: androidx.compose.ui.geometry.Offset = androidx.compose.ui.geometry.Offset.Zero
)