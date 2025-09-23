package com.example.exercise2slot3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import com.example.exercise2slot3.ui.theme.DragOverlayColor

@Composable
fun TrashBin(
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp) // Increased size for better UX
            .clip(CircleShape)
            .background(
                if (isHighlighted) 
                    MaterialTheme.colorScheme.error.copy(alpha = 0.9f)
                else 
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Note",
            tint = Color.White,
            modifier = Modifier.size(36.dp) // Increased icon size
        )
        
        // Add pulsing effect when highlighted
        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun DragOverlay(
    trashBinPosition: Offset,
    trashBinSize: IntSize,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DragOverlayColor)
    ) {
        // Enhanced highlight area around trash bin
        if (trashBinSize.width > 0 && trashBinSize.height > 0) {
            Box(
                modifier = Modifier
                    .offset(
                        x = maxOf(0.dp, (trashBinPosition.x - 30).dp),
                        y = maxOf(0.dp, (trashBinPosition.y - 30).dp)
                    )
                    .size(
                        width = (trashBinSize.width + 60).dp,
                        height = (trashBinSize.height + 60).dp
                    )
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.4f))
            )
            
            // Inner pulsing circle
            Box(
                modifier = Modifier
                    .offset(
                        x = maxOf(0.dp, (trashBinPosition.x - 10).dp),
                        y = maxOf(0.dp, (trashBinPosition.y - 10).dp)
                    )
                    .size(
                        width = (trashBinSize.width + 20).dp,
                        height = (trashBinSize.height + 20).dp
                    )
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }
    }
}