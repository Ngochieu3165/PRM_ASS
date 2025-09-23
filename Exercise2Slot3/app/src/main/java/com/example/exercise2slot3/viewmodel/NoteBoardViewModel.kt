package com.example.exercise2slot3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.exercise2slot3.data.Note
import com.example.exercise2slot3.data.DragInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.sqrt

class NoteBoardViewModel : ViewModel() {
    
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()
    
    private val _dragInfo = MutableStateFlow(DragInfo())
    val dragInfo: StateFlow<DragInfo> = _dragInfo.asStateFlow()
    
    private val _deletedNote = MutableStateFlow<Note?>(null)
    val deletedNote: StateFlow<Note?> = _deletedNote.asStateFlow()
    
    // Theme state - null means follow system, true = dark, false = light
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()
    
    fun toggleTheme() {
        _isDarkTheme.value = when (_isDarkTheme.value) {
            null -> true  // System -> Dark
            true -> false // Dark -> Light
            false -> null // Light -> System
        }
    }
    
    fun updateInputText(text: String) {
        _inputText.value = text
    }
    
    fun addNote() {
        val text = _inputText.value.trim()
        if (text.isNotEmpty()) {
            val newNote = Note(text = text)
            _notes.value = _notes.value + newNote
            _inputText.value = ""
        }
    }
    
    fun toggleNoteSelection(noteId: String) {
        _notes.value = _notes.value.map { note ->
            if (note.id == noteId) {
                note.copy(isSelected = !note.isSelected)
            } else {
                note
            }
        }
    }
    
    fun startDrag(note: Note) {
        _dragInfo.value = DragInfo(
            isDragging = true,
            draggedNote = note
        )
    }
    
    fun handleDrop(dropOffset: Offset, trashBinPosition: Offset, trashBinSize: IntSize) {
        val draggedNote = _dragInfo.value.draggedNote
        
        if (draggedNote != null) {
            // Simple check: if dragged more than 200px to the right and 300px down
            // This indicates user is trying to drag towards trash bin area
            val isDroppedNearTrashBin = dropOffset.x > 200f && dropOffset.y > 300f
            
            if (isDroppedNearTrashBin) {
                // Remove the note and store it for potential undo
                _deletedNote.value = draggedNote
                _notes.value = _notes.value.filter { it.id != draggedNote.id }
            }
        }
        
        // End drag state
        _dragInfo.value = DragInfo()
    }
    
    private fun isOverTrashBin(
        dropOffset: Offset, 
        trashBinPosition: Offset, 
        trashBinSize: IntSize
    ): Boolean {
        if (trashBinSize.width == 0 || trashBinSize.height == 0) return false
        
        // Calculate the center of the trash bin
        val trashBinCenterX = trashBinPosition.x + trashBinSize.width / 2f
        val trashBinCenterY = trashBinPosition.y + trashBinSize.height / 2f
        
        // Calculate distance from drop point to trash bin center
        val deltaX = dropOffset.x - trashBinCenterX
        val deltaY = dropOffset.y - trashBinCenterY
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
        
        // Check if within trash bin radius (with generous margin for easier dropping)
        val trashBinRadius = (trashBinSize.width / 2f) + 100 // 100px extra margin
        
        return distance <= trashBinRadius
    }
    
    fun undoDelete() {
        _deletedNote.value?.let { deletedNote ->
            _notes.value = _notes.value + deletedNote
            _deletedNote.value = null
        }
    }
    
    fun clearDeletedNote() {
        _deletedNote.value = null
    }
}