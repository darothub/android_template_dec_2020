package com.peacedude.lassod_tailor_app.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.peacedude.lassod_tailor_app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
internal class SplashScreenActivityTest {
    @get: Rule
    val activityRule: ActivityScenarioRule<SplashScreenActivity> = ActivityScenarioRule(SplashScreenActivity::class.java)

    @Test
    fun app_logo_is_displayed() {
        onView(withId(R.id.app_logo))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}
