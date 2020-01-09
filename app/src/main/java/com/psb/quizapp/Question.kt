package com.psb.quizapp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.StringRes
import com.google.firebase.firestore.FirebaseFirestore


data class Question(
    var id: String = "",
    var text: String = "",
    var optionA: String = "",
    var optionB: String = "",
    var optionC: String = "",
    var optionD: String = "",
    var answer: String = ""
)




