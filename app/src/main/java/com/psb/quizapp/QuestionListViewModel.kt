package com.psb.quizapp

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.type.Date
import java.sql.Time


class QuestionListViewModel:ViewModel() {

    val questions = mutableListOf<Question>()


    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    private val userReference = db.collection("users").document(currentUser)


    fun getQuestionsData(callback:(data:MutableList<Question>) -> Unit) {

        questions.clear()

        val docRef = db.collection("modules/TSLEC/questions")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")

                    var questionID = document.id

                    var questionData = document.data as  Map<String, Any>

                    val questionText = questionData["questionText"] as String

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

                    var answer = ""

                    when {
                        choiceAValue -> answer = "a"
                        choiceBValue -> answer = "b"
                        choiceCValue -> answer = "c"
                        choiceDValue -> answer = "d"
                    }

                    val question = Question()

                    question.id = questionID
                    question.text = questionText
                    question.optionA = choiceAText
                    question.optionB = choiceBText
                    question.optionC = choiceCText
                    question.optionD = choiceDText
                    question.answer = answer

                    questions.add(question)
                }
                Log.d(ContentValues.TAG, "QUESTIONS : $questions")
                callback(questions)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }



    }

    fun uploadCurrentScore(score:Int){

        val timestamp = java.sql.Timestamp(System.currentTimeMillis())

        val scoreData = hashMapOf(
            "timestamp" to timestamp,
            "score" to score
        )

        userReference.collection("scores").add(scoreData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun getPastScoresForUser(callback: (data: List<Map<String,Any>>) -> Unit) {

        userReference.collection("scores").orderBy("timestamp", Query.Direction.DESCENDING).limit(3).get().addOnSuccessListener { documents ->
            var scoreList = mutableListOf<Map<String,Any>>()
            for (document in documents) {
                Log.d(TAG, "${document.id} => ${document.data}")
                val scoreData = document.data as Map<String,Any>
                scoreList.add(scoreData)
            }
            callback(scoreList)
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }



    }

}
