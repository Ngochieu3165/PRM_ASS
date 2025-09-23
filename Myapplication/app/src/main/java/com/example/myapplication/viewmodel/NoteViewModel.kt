package com.example.myapplication.viewmodel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Note

class NoteViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> get() = _notes

    private var lastRemoved: Pair<Note, Int>? = null

    fun addNote(text: String) {
        _notes.add(0, Note(text = text))
    }

    fun toggleSelect(note: Note) {
        val idx = _notes.indexOfFirst { it.id == note.id }
        if (idx >= 0) {
            _notes[idx] = _notes[idx].copy(isSelected = !_notes[idx].isSelected)
        }
    }

    fun remove(note: Note) {
        val idx = _notes.indexOfFirst { it.id == note.id }
        if (idx >= 0) {
            lastRemoved = _notes[idx] to idx
            _notes.removeAt(idx)
        }
    }

    fun undo() {
        lastRemoved?.let { (note, idx) ->
            _notes.add(idx.coerceIn(0, _notes.size), note)
            lastRemoved = null
        }
    }
}
