package com.example.myapplication.model

data class Note(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isSelected: Boolean = false
)
