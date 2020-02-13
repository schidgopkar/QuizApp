package com.psb.quizapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule


import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.not

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

import org.hamcrest.CoreMatchers.not






/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.psb.quizapp", appContext.packageName)
    }

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testCorrectQuesTextIsBeingDisplayed(){

        val questionModel = QuestionListViewModel()

        questionModel.getQuestionsData {

        }

        Thread.sleep(5000)

        onView(withId(R.id.question_text_view))
            .check(matches(withText(questionModel.questions.first().text)))


    }

    @Test
    fun testFirstQuestionCorrectOptionTextBeingDisplayedForRadioButtonOptionA(){

        val questionModel = QuestionListViewModel()

        questionModel.getQuestionsData {

        }

        Thread.sleep(5000)

        onView(withId(R.id.radio_1))
            .check(matches(withText(questionModel.questions.first().optionA)))

    }



    @Test
    fun testFirstQuestion_CorrectAnswer_DisplaysCorrectAnswerNotification(){

        val questionModel = QuestionListViewModel()

        questionModel.getQuestionsData {

        }

        Thread.sleep(5000)

        var clickID = R.id.radio_1

        if (questionModel.questions.first().answer == "b"){
            clickID = R.id.radio_2
        }
        if (questionModel.questions.first().answer == "c"){
            clickID = R.id.radio_3
        }
        if (questionModel.questions.first().answer == "d"){
            clickID = R.id.radio_4
        }

        onView(withId(clickID)).perform(click())

        onView(withText("Great. It is the correct answer. Current score is 1")).inRoot(withDecorView(org.hamcrest.Matchers.not(org.hamcrest.Matchers.`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
        
    }




}
