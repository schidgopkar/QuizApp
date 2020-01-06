package com.psb.quizapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import java.io.Serializable

private const val EXTRA_USER_ANSWERS_MAP = "com.psb.quizapp.user_answers_map"
private const val EXTRA_ANSWERS_MAP = "com.psb.quizapp.answers_map"

class ResultsActivity : AppCompatActivity() {

    private lateinit var resultProgressBar: ProgressBar

    private var userAnswers = hashMapOf<Int, String>()
    private var answers = hashMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        resultProgressBar = findViewById(R.id.result_progress_bar)

        userAnswers = intent.getSerializableExtra(EXTRA_USER_ANSWERS_MAP) as HashMap<Int, String>
        answers = intent.getSerializableExtra(EXTRA_ANSWERS_MAP) as HashMap<Int, String>

        Log.d(ContentValues.TAG, "Results Activity user answer data: $userAnswers")
        Log.d(ContentValues.TAG, "Results Activity answers data: $answers")

        checkAnswers()

    }


    companion object {
        fun newIntent(
            packagedContext: Context,
            userAnswers: HashMap<Int, String>,
            answers: HashMap<Int, String>
        ): Intent {
            return Intent(packagedContext, ResultsActivity::class.java).apply {
                putExtra(EXTRA_USER_ANSWERS_MAP, userAnswers)
                putExtra(EXTRA_ANSWERS_MAP, answers)
            }
        }
    }


    private fun checkAnswers(){

        var correctAnswersCount = 0

        for((questionNumber, answer) in answers){
            if(userAnswers[questionNumber] == answer){
                correctAnswersCount ++
            }
        }

        val percentageCorrect = (correctAnswersCount.toDouble()/(answers.size)) * 100


        Log.d(ContentValues.TAG, "Correct answers count: $percentageCorrect")

        resultProgressBar.progress = percentageCorrect.toInt()


    }

}
