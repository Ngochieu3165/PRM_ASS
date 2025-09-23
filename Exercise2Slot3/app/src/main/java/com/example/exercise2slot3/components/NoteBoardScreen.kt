package com.example.exercise2slot3.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exercise2slot3.data.Note
import com.example.exercise2slot3.ui.theme.*
import com.example.exercise2slot3.viewmodel.NoteBoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteBoardScreen(
    viewModel: NoteBoardViewModel = viewModel()
) {
    val notes by viewModel.notes.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val dragInfo by viewModel.dragInfo.collectAsState()
    val themeState by viewModel.isDarkTheme.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var trashBinPosition by remember { mutableStateOf(Offset.Zero) }
    var trashBinSize by remember { mutableStateOf(IntSize.Zero) }
    
    // Determine current theme
    val isDarkTheme = themeState ?: isSystemInDarkTheme()

    LaunchedEffect(viewModel.deletedNote.collectAsState().value) {
        viewModel.deletedNote.value?.let { deletedNote ->
            val result = snackbarHostState.showSnackbar(
                message = "Note deleted. Undo?",
                actionLabel = "Undo",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            }
            viewModel.clearDeletedNote()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Custom Toolbar with Enhanced Shadow and Theme Switch
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Note Board",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Theme Switch Button with indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleTheme() },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = when (themeState) {
                                    null -> Icons.Default.Brightness6  // System
                                    true -> Icons.Default.DarkMode     // Dark
                                    false -> Icons.Default.LightMode  // Light
                                },
                                contentDescription = when (themeState) {
                                    null -> "System Theme"
                                    true -> "Dark Mode"
                                    false -> "Light Mode"
                                }
                            )
                        }
                        
                        Text(
                            text = when (themeState) {
                                null -> "Auto"
                                true -> "Dark"
                                false -> "Light"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Section with Enhanced Styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = viewModel::updateInputText,
                    label = { Text("Enter note") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { viewModel.addNote() },
                    enabled = inputText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Add",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes List with Enhanced Spacing
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = notes,
                    key = { it.id }
                ) { note ->
                    NoteItem(
                        note = note,
                        onLongPress = { viewModel.toggleNoteSelection(note.id) },
                        onDragStart = { viewModel.startDrag(note) },
                        onDragEnd = { offset -> 
                            // Convert relative offset to absolute screen position
                            viewModel.handleDrop(offset, trashBinPosition, trashBinSize)
                        },
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }

        // Trash Bin
        TrashBin(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .onGloballyPositioned { coordinates ->
                    trashBinPosition = coordinates.positionInRoot()
                    trashBinSize = coordinates.size
                },
            isHighlighted = dragInfo.isDragging
        )

        // Drag Overlay
        if (dragInfo.isDragging) {
            DragOverlay(
                trashBinPosition = trashBinPosition,
                trashBinSize = trashBinSize
            )
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}