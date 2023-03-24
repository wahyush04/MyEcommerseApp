package com.wahyush04.androidphincon.ui.login

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wahyush04.androidphincon.EspressoIdlingResource
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.androidphincon.ui.main.MainActivity
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.internal.verification.Description
import java.util.regex.Matcher

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class LoginActivityTest{

    @get:Rule
        val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun a_login_success() {
        Intents.init()
        onView(withId(R.id.edt_email_login))
            .perform(typeText("infocus@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.edt_password_login))
            .perform(typeText("1234567"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        SystemClock.sleep(2000)
        Intents.intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rv_product_list_home)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun b_logout() {
        Intents.release()
        Intents.init()
        SystemClock.sleep(1000)
        onView(withId(R.id.navigation_profile)).perform(click())
        onView(withId(R.id.cv_logout)).perform(click())
        SystemClock.sleep(1000)
        Intents.intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.btn_login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Intents.release()
    }

    @Test
    fun c_register_success() {
        Intents.init()
        onView(withId(R.id.btn_to_signup)).perform(click())
        onView(withId(R.id.edt_email))
            .perform(typeText("uitest14@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.edt_password))
            .perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.edt_password_confirm))
            .perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.edt_name))
            .perform(typeText("uitest"), closeSoftKeyboard())
        onView(withId(R.id.edt_phone))
            .perform(typeText("1234567890"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        SystemClock.sleep(2500)
        Intents.intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.btn_login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}