package com.psb.quizapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_result_list.*
import android.content.Intent
import com.google.common.base.Strings
import com.google.firebase.Timestamp

const val TAG = "ResultListFragment"

class ResultListFragment : Fragment() {

    private var userAnswers = hashMapOf<String, String>()

    var correctAnswerCount = 0

    private lateinit var gradeTextView:TextView
    private lateinit var firstScoreTextView: TextView
    private  lateinit var secondScoreTextView: TextView
    private lateinit var thirdScoreTextView: TextView

    private lateinit var resultRecyclerView: RecyclerView
    private var adapter: ResultAdapter? = null

    private val questionListViewModel: QuestionListViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userAnswers = arguments?.getSerializable("userAnswers") as HashMap<String, String>

        val view = inflater.inflate(R.layout.fragment_result_list, container, false)

        resultRecyclerView =
            view.findViewById(R.id.result_recycler_view) as RecyclerView
        resultRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        gradeTextView= view.findViewById(R.id.grade_text_view)
        firstScoreTextView = view.findViewById(R.id.first_score_textview)
        secondScoreTextView = view.findViewById(R.id.second_score_textview)
        thirdScoreTextView = view.findViewById(R.id.third_score_textview)

        return view
    }

    private fun updateUI() {
        questionListViewModel.getQuestionsData { data ->
            adapter = ResultAdapter(data)
            resultRecyclerView.adapter = adapter
            data.forEach { question ->
                if(userAnswers[question.id] == question.answer){
                    correctAnswerCount ++

                }
            }
            gradeTextView.text = "Grade $correctAnswerCount out of ${data.size}"
            questionListViewModel.uploadCurrentScore(correctAnswerCount)
        }

        questionListViewModel.getPastScoresForUser {scoreList ->
            for(score in scoreList){
                val firstTimestamp = scoreList[0]["timestamp"] as Timestamp
                val secondTimestamp = scoreList[1]["timestamp"] as Timestamp
                val thirdTimestamp = scoreList[2]["timestamp"] as Timestamp
                val firstScore = scoreList[0]["score"]
                val secondScore = scoreList[1]["score"]
                val thirdScore = scoreList[2]["score"]
                firstScoreTextView.text =  "${firstTimestamp.toDate()}  ->  $firstScore"
                secondScoreTextView.text = "${secondTimestamp.toDate()}  ->  $secondScore"
                thirdScoreTextView.text = "${thirdTimestamp.toDate()}  ->  $thirdScore"
            }
        }
    }

    private inner class ResultHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var question: Question

        private val questionTextView: TextView = itemView.findViewById(R.id.question_text)
        private val radioButton1: RadioButton = itemView.findViewById(R.id.result_list_radio_1)
        private val radioButton2: RadioButton = itemView.findViewById(R.id.result_list_radio_2)
        private val radioButton3: RadioButton = itemView.findViewById(R.id.result_list_radio_3)
        private val radioButton4: RadioButton = itemView.findViewById(R.id.result_list_radio_4)
        private val answerTextView: TextView = itemView.findViewById(R.id.answer_text)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(question: Question) {


            this.question = question
            questionTextView.text = this.question.text
            radioButton1.text = question.optionA
            radioButton2.text = question.optionB
            radioButton3.text = question.optionC
            radioButton4.text = question.optionD
            answerTextView.text = "The correct answer is: ${question.answer}"


            when {
                userAnswers[question.id] == "a" -> radioButton1.isChecked = true
                userAnswers[question.id] == "b" -> radioButton2.isChecked = true
                userAnswers[question.id] == "c" -> radioButton3.isChecked = true
                userAnswers[question.id] == "d" -> radioButton4.isChecked = true
            }

            radioButton1.isEnabled = false
            radioButton2.isEnabled = false
            radioButton3.isEnabled = false
            radioButton4.isEnabled = false

        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${question.id} clicked!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private inner class ResultAdapter(var questions: List<Question>) :
        RecyclerView.Adapter<ResultHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ResultHolder {
            val view = layoutInflater.inflate(R.layout.list_item_result, parent, false)
            return ResultHolder(view)
        }

        override fun onBindViewHolder(holder: ResultHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)
        }

        override fun getItemCount() = questions.size
    }

    companion object {
        fun newInstance(userAnswers:HashMap<String, String>): ResultListFragment {
            val resultFragment = ResultListFragment()
            val bundle = Bundle().apply {
                putSerializable("userAnswers", userAnswers)
            }
            resultFragment.arguments = bundle
            return resultFragment
        }
    }



}
