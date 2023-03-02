package com.wahyush04.androidphincon.ui.login

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.wahyush04.androidphincon.EspressoIdlingResource
import com.wahyush04.androidphincon.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest{
    @get:Rule
    //    val activity = ActivityScenarioRule(LoginActivity::class.java)
    var activityRule: ActivityTestRule<LoginActivity>
            = ActivityTestRule(LoginActivity::class.java)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login() {
        // Type text and then press the button.
        onView(withId(R.id.edt_email))
            .perform(typeText("infocus@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.edt_password_login))
            .perform(typeText("123456"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
    }

    @Test
    fun toSignUp() {
        // Type text and then press the button.
        onView(withId(R.id.btn_to_signup)).perform(click())
    }

}