package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen { playerName ->
                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra("playerName", playerName)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MainScreen(onStartQuiz: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onStartQuiz(name) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }
    }
}
