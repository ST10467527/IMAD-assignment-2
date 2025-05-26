package vcmsa.ci.flashcards

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestionActivity : AppCompatActivity() {

    data class Question(val text: String, val answer: Boolean)

    val questionList = listOf(
        Question("South Africa is completely surrounded by water..", false),
        Question("South Africa is known for its rich biodiversity and is home to the Big Five animals..", true),
        Question("Table Mountain is located in Johannesburg.", false),
        Question("The official currency of South Africa is the Rand...", true),
        Question("Nelson Mandela was the first black president of South Africa.", true)  // ✅ New Question
    )

     var currentIndex = 0
     var score = 0
     val selectedAnswers = mutableListOf<Boolean>()

    private lateinit var tvQuestion: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnTrue: Button
    private lateinit var btnFalse: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        tvQuestion = findViewById(R.id.tvQuestion)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnTrue = findViewById(R.id.btnTrue)
        btnFalse = findViewById(R.id.btnFalse)
        btnNext = findViewById(R.id.btnNext)

        loadQuestion()

        btnTrue.setOnClickListener { checkAnswer(true) }
        btnFalse.setOnClickListener { checkAnswer(false) }
        btnNext.setOnClickListener { goToNextQuestion() }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadQuestion() {
        if (currentIndex >= questionList.size) {
            // Quiz finished, go to score screen
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("SCORE", score)
            intent.putExtra("TOTAL", questionList.size)
            intent.putExtra("SELECTED", selectedAnswers.toBooleanArray())
            intent.putExtra("QUESTIONS", questionList.map { it.text }.toTypedArray())
            intent.putExtra("ANSWERS", questionList.map { it.answer }.toBooleanArray())
            startActivity(intent)
            finish()
            return
        }

        val question = questionList[currentIndex]
        tvQuestion.text = question.text
        tvFeedback.text = ""
        btnTrue.isEnabled = true
        btnFalse.isEnabled = true
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionList[currentIndex].answer
        if (userAnswer == correctAnswer) {
            tvFeedback.text = "Correct!"
            tvFeedback.setTextColor(getColor(android.R.color.holo_green_dark))
            score++
        } else {
            tvFeedback.text = "Incorrect"
            tvFeedback.setTextColor(getColor(android.R.color.holo_red_dark))
        }

        selectedAnswers.add(userAnswer)

        btnTrue.isEnabled = false
        btnFalse.isEnabled = false
    }

    private fun goToNextQuestion() {
        currentIndex++
        loadQuestion()
    }
}
