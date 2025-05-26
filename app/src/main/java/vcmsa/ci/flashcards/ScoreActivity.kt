package vcmsa.ci.flashcards

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScoreActivity : AppCompatActivity() {

    private lateinit var tvFinalScore: TextView
    private lateinit var tvMotivationalMsg: TextView
    private lateinit var tvReview: TextView
    private lateinit var btnReviewAnswers: Button
    private lateinit var btnRetake: Button
    private lateinit var btnExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        
        tvFinalScore = findViewById(R.id.tvFinalScore)
        tvMotivationalMsg = findViewById(R.id.tvMotivationalMsg)
        tvReview = findViewById(R.id.tvReview)
        btnReviewAnswers = findViewById(R.id.btnReviewAnswers)
        btnRetake = findViewById(R.id.btnRetake)
        btnExit = findViewById(R.id.btnExit)


        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        val selectedAnswers = intent.getBooleanArrayExtra("SELECTED") ?: BooleanArray(0)


        val feedback = when {
            score == total -> "Excellent work!"
            score > total / 2 -> "Great effort!"
            else -> "Keep practicing!"
        }


        tvFinalScore.text = "Your score: $score / $total"
        tvMotivationalMsg.text = feedback
        tvReview.text = ""


        val questionList = listOf(
            Question("The Great Wall of China can be seen from space.", false),
            Question("World War I ended in 1918.", true),
            Question("Napoleon was a Roman emperor.", false),
            Question("The Declaration of Independence was signed in 1776.", true),
            Question("The Eiffel Tower is located in Berlin.", false) // Added 5th question example
        )


        btnReviewAnswers.setOnClickListener {
            val builder = StringBuilder()
            for (i in questionList.indices) {
                val userAnswer = selectedAnswers.getOrNull(i)?.toString() ?: "No answer"
                val correctAnswer = questionList[i].answer.toString()
                builder.append("Q${i + 1}: ${questionList[i].text}\n")
                builder.append("Your Answer: $userAnswer\n")
                builder.append("Correct Answer: $correctAnswer\n\n")
            }
            tvReview.text = builder.toString()
        }


        btnRetake.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnExit.setOnClickListener {
            finishAffinity()
        }
    }

    data class Question(val text: String, val answer: Boolean)
}
