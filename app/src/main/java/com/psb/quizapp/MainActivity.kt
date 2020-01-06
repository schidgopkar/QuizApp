package com.psb.quizapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    var question = Question()

    private var currentQuestionNumber = 0

    var questions = mutableListOf(mapOf<String, Any>())

    private var questionData = mapOf<String, Any>()

    private var userAnswers = hashMapOf<Int, String>()
    private var answers = hashMapOf<Int, String>()

    private lateinit var questionNumber:TextView
    private lateinit var questionTextView: TextView

    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioButton3: RadioButton
    private lateinit var radioButton4: RadioButton

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private lateinit var submitButton:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        question.getAllQuestionsData { data, answers ->
            if (data != null) {
                questions = data
                Log.d(ContentValues.TAG, "question data: $data")
                updateQuestion()
                this.answers = answers
            }
        }

        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        submitButton = findViewById(R.id.submit_button)

        questionNumber = findViewById(R.id.question_number_texview)

        questionTextView = findViewById(R.id.question_text_view)

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
                currentQuestionNumber = (currentQuestionNumber + 1) % questions.size
                updateQuestion()
            Log.d(ContentValues.TAG, "Choices data: ${questionData.size}")
        }

        submitButton.setOnClickListener {
            val intent = ResultsActivity.newIntent(this@MainActivity, userAnswers, answers)
            startActivity(intent)
        }

    }


    private fun updateQuestion(){

            questionData = questions[currentQuestionNumber]

            val questionText = questionData["questionText"] as String?

            val choices = questionData?.get("options") as Map<String, Map<String,String>>?

            val choiceA = choices?.get("a") as Map<String, Any>?
            val choiceAText = choiceA?.get("optionText") as String
            val choiceAValue =  choiceA?.get("optionValue") as Boolean

            val choiceB = choices?.get("b") as Map<String, Any>?
            val choiceBText = choiceB?.get("optionText") as String
            val choiceBValue =  choiceB?.get("optionValue") as Boolean

            val choiceC = choices?.get("c") as Map<String, Any>?
            val choiceCText = choiceC?.get("optionText") as String
            val choiceCValue =  choiceC?.get("optionValue") as Boolean

            val choiceD = choices?.get("d") as Map<String, Any>?
            val choiceDText = choiceD?.get("optionText") as String
            val choiceDValue =  choiceD?.get("optionValue") as Boolean


            questionNumber.text = "Question: ${currentQuestionNumber + 1}"

            questionTextView.text = questionText

            radioButton1.text = choiceAText
            radioButton2.text = choiceBText
            radioButton3.text = choiceCText
            radioButton4.text = choiceDText

            Log.d(ContentValues.TAG, "Choices data: $choiceAValue")

            when {
                userAnswers[currentQuestionNumber] == "a" -> radioButton1.isChecked = true
                userAnswers[currentQuestionNumber] == "b" -> radioButton2.isChecked = true
                userAnswers[currentQuestionNumber] == "c" -> radioButton3.isChecked = true
                userAnswers[currentQuestionNumber] == "d" -> radioButton4.isChecked = true
                else -> {
                    radioButton1.isChecked = false
                    radioButton2.isChecked = false
                    radioButton3.isChecked = false
                    radioButton4.isChecked = false
                }
            }




    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_1 ->
                    if (checked) {
                        Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()
                        userAnswers[currentQuestionNumber] = "a"
                    }
                R.id.radio_2 ->
                    if (checked) {
                        Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()
                        userAnswers[currentQuestionNumber] = "b"
                    }
                R.id.radio_3 ->
                    if (checked) {
                        Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()
                        userAnswers[currentQuestionNumber] = "c"
                    }
                R.id.radio_4 ->
                    if (checked) {
                        Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()
                        userAnswers[currentQuestionNumber] = "d"
                    }
            }
        }
    }




}
