package com.psb.quizapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import java.io.Serializable

private const val EXTRA_USER_ANSWERS_MAP = "com.psb.quizapp.user_answers_map"

class ResultsActivity : AppCompatActivity() {

//    private lateinit var resultProgressBar: ProgressBar

    private var userAnswers = hashMapOf<String, String>()
    private var answers = hashMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val actionBar = supportActionBar
        actionBar!!.title = "Results"

//        resultProgressBar = findViewById(R.id.result_progress_bar)

        userAnswers = intent.getSerializableExtra(EXTRA_USER_ANSWERS_MAP) as HashMap<String, String>

        Log.d(ContentValues.TAG, "Results Activity user answer data: $userAnswers")

        checkAnswers()

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = ResultListFragment.newInstance(userAnswers)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_retake_quiz -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    companion object {
        fun newIntent(
            packagedContext: Context,
            userAnswers: HashMap<String, String>
        ): Intent {
            return Intent(packagedContext, ResultsActivity::class.java).apply {
                putExtra(EXTRA_USER_ANSWERS_MAP, userAnswers)
            }
        }
    }


    private fun checkAnswers(){

        var correctAnswersCount = 0

//        for((questionNumber, answer) in answers){
//            if(userAnswers[questionNumber] == answer){
//                correctAnswersCount ++
//            }
//        }

        val percentageCorrect = (correctAnswersCount.toDouble()/(answers.size)) * 100


        Log.d(ContentValues.TAG, "Correct answers count: $percentageCorrect")

//        resultProgressBar.progress = percentageCorrect.toInt()


    }

}
