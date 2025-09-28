package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class QuizActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()

    companion object {
        private const val TAG = "QuizActivity"
        private const val KEY_INDEX = "currentIndex"
        private const val KEY_SCORE = "score"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // Nhận dữ liệu từ Intent
        val playerName = intent.getStringExtra("playerName") ?: "Player"
        viewModel.setPlayerName(playerName)

        if (savedInstanceState != null) {
            viewModel.restoreState(
                savedInstanceState.getInt(KEY_INDEX, 0),
                savedInstanceState.getInt(KEY_SCORE, 0)
            )
        }

        setContent {
            MaterialTheme {
                QuizScreen(viewModel = viewModel) { correct ->
                    val msg = if (correct) "Correct!" else "Incorrect!"
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Ghi log vòng đời
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, viewModel.currentIndex)
        outState.putInt(KEY_SCORE, viewModel.score)
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel, onAnswer: (Boolean) -> Unit) {
    val question = viewModel.getCurrentQuestion()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Welcome, ${viewModel.playerName}", style = MaterialTheme.typography.titleMedium)
        Text(text = question.text, style = MaterialTheme.typography.titleLarge)

        question.options.forEachIndexed { index, option ->
            Button(
                onClick = {
                    val correct = viewModel.checkAnswer(index)
                    onAnswer(correct)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(option)
            }
        }

        Text("Score: ${viewModel.score}")

        Button(
            onClick = { viewModel.nextQuestion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next Question")
        }
    }
}
