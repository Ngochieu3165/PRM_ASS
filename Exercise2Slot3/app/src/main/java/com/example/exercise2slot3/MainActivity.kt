package com.example.exercise2slot3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exercise2slot3.ui.theme.Exercise2Slot3Theme
import com.example.exercise2slot3.components.NoteBoardScreen
import com.example.exercise2slot3.viewmodel.NoteBoardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NoteBoardViewModel = viewModel()
            val themeState by viewModel.isDarkTheme.collectAsState()
            
            Exercise2Slot3Theme(
                darkTheme = themeState ?: isSystemInDarkTheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoteBoardScreen(viewModel = viewModel)
                }
            }
        }
    }
}
