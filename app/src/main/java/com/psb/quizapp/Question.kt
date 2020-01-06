package com.psb.quizapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class Question {


    // Access a Cloud Firestore instance from your Activity
    private val db = FirebaseFirestore.getInstance()

    var questions = mutableListOf(mapOf<String, Any>())

    private var answers = hashMapOf<Int, String>()


    fun getAllQuestionsData(callback:(data:MutableList<Map<String, Any>>,answers:HashMap<Int, String>) -> Unit){

        questions.clear()

        val docRef = db.collection("modules/TSLEC/questions")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    questions.add(document.data)
                }
                getAnswers()
                callback(questions, answers)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }


    private fun getAnswers(){

        questions.forEachIndexed { index, question ->

            val choices = question["options"] as Map<String, Map<String,String>>

            val choiceA = choices?.get("a") as Map<String, Any>?
            val choiceAValue =  choiceA?.get("optionValue") as Boolean

            val choiceB = choices?.get("b") as Map<String, Any>?
            val choiceBValue =  choiceB?.get("optionValue") as Boolean

            val choiceC = choices?.get("c") as Map<String, Any>?
            val choiceCValue =  choiceC?.get("optionValue") as Boolean

            val choiceD = choices?.get("d") as Map<String, Any>?
            val choiceDValue =  choiceD?.get("optionValue") as Boolean

            when{
                choiceAValue -> answers[index] = "a"
                choiceBValue -> answers[index] = "b"
                choiceCValue -> answers[index] = "c"
                choiceDValue -> answers[index] = "d"
            }

        }


    }


}