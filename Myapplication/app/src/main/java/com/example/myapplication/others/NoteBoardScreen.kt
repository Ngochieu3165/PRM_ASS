package com.example.myapplication.others

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteBoardContent(
    notes: List<Note>,
    text: String,
    onTextChange: (String) -> Unit,
    onAddNote: () -> Unit,
    onRemoveNote: (Note) -> Unit,
    onToggleSelect: (Note) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🔹 Thêm biến trạng thái để biết có đang kéo không
    var isDragging by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Note Board", style = MaterialTheme.typography.titleLarge) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddNote()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) { Text("+") }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // ======= Nội dung chính =======
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    label = { Text("Enter note") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteItem(
                            note = note,
                            onLongPress = {
                                // 🔹 Giả lập bắt đầu kéo
                                isDragging = true
                                onToggleSelect(note)
                            },
                            onDragToTrash = {
                                onRemoveNote(note)
                                isDragging = false
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Note deleted",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        // gọi undo ở ViewModel nếu cần
                                    }
                                }
                            }
                        )
                    }
                }
            }

            // ======= Trash Bin icon =======
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Trash Bin",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(56.dp)
                    .background(Color.LightGray, CircleShape)
                    .padding(16.dp)
            )


            // ======= Overlay mờ khi đang kéo =======
            if (isDragging) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                )
            }
        }
    }
}

@Composable
fun NoteBoardScreen(vm: NoteViewModel) {
    var text by remember { mutableStateOf("") }

    NoteBoardContent(
        notes = vm.notes,
        text = text,
        onTextChange = { text = it },
        onAddNote = {
            if (text.isNotBlank()) {
                vm.addNote(text.trim())
                text = ""
            }
        },
        onRemoveNote = { vm.remove(it) },
        onToggleSelect = { vm.toggleSelect(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun NoteBoardPreview() {
    val sampleNotes = listOf(
        Note(id = 1L, text = "Sample note 1"),
        Note(id = 2L, text = "Sample note 2"),
        Note(id = 3L, text = "Another example")
    )
    NoteBoardContent(
        notes = sampleNotes,
        text = "",
        onTextChange = {},
        onAddNote = {},
        onRemoveNote = {},
        onToggleSelect = {}
    )
}
