package com.psb.quizapp

import android.app.ProgressDialog.show
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.custom_toast.*

class MainActivity : AppCompatActivity() {

    private var currentQuestionNumber = 0

    private var questions = mutableListOf<Question>()

    private var userAnswers = hashMapOf<String, String>()

    private lateinit var questionNumber:TextView
    private lateinit var questionTextView: TextView

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioButton3: RadioButton
    private lateinit var radioButton4: RadioButton

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private lateinit var submitButton:Button

    private var questionListViewModel: QuestionListViewModel = QuestionListViewModel()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar

        actionBar!!.setIcon(R.drawable.psb_logo)

        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        submitButton = findViewById(R.id.submit_button)

        questionNumber = findViewById(R.id.question_number_texview)

        questionTextView = findViewById(R.id.question_text_view)

        radioGroup = findViewById(R.id.radio_group)

        radioButton1 = findViewById(R.id.radio_1)
        radioButton2 = findViewById(R.id.radio_2)
        radioButton3 = findViewById(R.id.radio_3)
        radioButton4 = findViewById(R.id.radio_4)

        prevButton.setOnClickListener {
            if (currentQuestionNumber > 0){
                currentQuestionNumber -= 1
                updateQuestion()
            }
        }

        nextButton.setOnClickListener {
                currentQuestionNumber = (currentQuestionNumber + 1) % questionListViewModel.questions.size
                updateQuestion()
        }

        submitButton.setOnClickListener {
            val intent = ResultsActivity.newIntent(this@MainActivity, userAnswers)
            startActivity(intent)
        }

        questionListViewModel.getQuestionsData {data ->
            questions = data
            questions.forEach{ question ->
                userAnswers[question.id] = ""
            }
            updateQuestion()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_retake_quiz -> {
            recreate()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun updateQuestion(){


            var question = questions[currentQuestionNumber]

            questionNumber.text = "Question: ${(currentQuestionNumber + 1)}"

            questionTextView.text = question.text

            radioButton1.text = question.optionA
            radioButton2.text = question.optionB
            radioButton3.text = question.optionC
            radioButton4.text = question.optionD

            when {
                userAnswers[questions[currentQuestionNumber].id] == "a" -> radioButton1.isChecked = true
                userAnswers[questions[currentQuestionNumber].id] == "b" -> radioButton2.isChecked = true
                userAnswers[questions[currentQuestionNumber].id] == "c" -> radioButton3.isChecked = true
                userAnswers[questions[currentQuestionNumber].id] == "d" -> radioButton4.isChecked = true

                else -> {
                    radioGroup.clearCheck()
                }
            }
    }



    private fun showAnswerToast(userAnswer:String){

        userAnswers[questions[currentQuestionNumber].id] = userAnswer

        val layout = layoutInflater.inflate(R.layout.custom_toast,custom_toast_container)

        val questionFeedbackTextView:TextView = layout.findViewById(R.id.question_feedback_text_view)

        val answerFeedbackImageView:ImageView = layout.findViewById(R.id.answer_feedback_image_view)

        val correctAnswer = questions[currentQuestionNumber].answer

        var correctAnswerCount = 0

        questions.forEach { question ->
            if (userAnswers[question.id] == question.answer) {
                correctAnswerCount++
            }
        }

        if(userAnswer == correctAnswer){
            questionFeedbackTextView.text = "Great. It is the correct answer. Current score is $correctAnswerCount"
        }else{
            questionFeedbackTextView.text = "Wrong. The correct answer is $correctAnswer"
            answerFeedbackImageView.setImageResource(R.drawable.wrong)
        }


        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.setGravity(Gravity.CENTER, 0,-50)
        myToast.view = layout//setting the view of custom toast layout
        myToast.show()


    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_1 ->
                    if (checked) {
                        val userAnswer = "a"
                        showAnswerToast(userAnswer)
                    }
                R.id.radio_2 ->
                    if (checked) {
                        val userAnswer = "b"
                        showAnswerToast(userAnswer)
                    }
                R.id.radio_3 ->
                    if (checked) {
                        val userAnswer = "c"
                        showAnswerToast(userAnswer)
                    }
                R.id.radio_4 ->
                    if (checked) {
                        val userAnswer = "d"
                        showAnswerToast(userAnswer)
                    }
            }
        }
    }




}
