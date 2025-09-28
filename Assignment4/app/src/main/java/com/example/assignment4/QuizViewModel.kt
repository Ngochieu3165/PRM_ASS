package com.example.quizapp

import androidx.lifecycle.ViewModel

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

class QuizViewModel : ViewModel() {

    var playerName: String = ""
        private set

    private val questions = listOf(
        Question("1. Thủ đô của Việt Nam là gì?", listOf("Hà Nội", "TP.HCM", "Đà Nẵng", "Huế"), 0),
        Question("2. 2 + 2 = ?", listOf("2", "3", "4", "5"), 2),
        Question("3. Android được phát triển bởi ai?", listOf("Google", "Microsoft", "Apple", "Samsung"), 0),
        Question("4. Năm nhuận có bao nhiêu ngày?", listOf("365", "366", "364", "367"), 1)
    )

    var currentIndex: Int = 0
        private set

    var score: Int = 0
        private set

    fun setPlayerName(name: String) {
        playerName = name
    }

    fun getCurrentQuestion(): Question = questions[currentIndex]

    fun checkAnswer(selectedIndex: Int): Boolean {
        val correct = questions[currentIndex].correctIndex == selectedIndex
        if (correct) {
            score++
            // Extra challenge: tự động sang câu tiếp
            nextQuestion()
        }
        return correct
    }

    fun nextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
        }
    }

    fun restoreState(index: Int, savedScore: Int) {
        currentIndex = index
        score = savedScore
    }
}
