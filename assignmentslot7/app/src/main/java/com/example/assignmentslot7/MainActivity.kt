package com.example.moviecatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.moviecatalog.ui.theme.MovieCatalogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieCatalogTheme {
                var gridMode by remember { mutableStateOf(false) }
                val columns = if (isTablet()) {
                    if (gridMode) 3 else 2
                } else {
                    if (gridMode) 2 else 1
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Movie Catalog") },
                            actions = {
                                IconButton(onClick = { gridMode = false }) {
                                    Icon(Icons.Default.List, contentDescription = "List View")
                                }
                                IconButton(onClick = { gridMode = true }) {
                                    Icon(Icons.Default.GridView, contentDescription = "Grid View")
                                }
                            }
                        )
                    }
                ) { padding ->
                    if (gridMode) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            contentPadding = PaddingValues(8.dp),
                            modifier = androidx.compose.ui.Modifier.padding(padding)
                        ) {
                            items(`MovieData.kt`.movies.size) { index ->
                                MovieCard(`MovieData.kt`.movies[index])
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            modifier = androidx.compose.ui.Modifier.padding(padding)
                        ) {
                            items(`MovieData.kt`.movies) { movie ->
                                MovieCard(movie)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isTablet(): Boolean {
        val metrics = resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        return widthDp >= 600
    }
}
