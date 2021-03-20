package com.peacedude.lassod_tailor_app.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.peacedude.lassod_tailor_app.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
internal class HomeFragmentTest {

    @Test
    fun app_logo_is_displayed() {
        val scenario = launchFragmentInContainer<TestHomeFragment>()
        onView(withId(R.id.login_btn))
            .check(matches(ViewMatchers.isDisplayed()))
    }
}
