package com.example.mpos

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mpos.ui.login.LoginScreenFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Testing {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getResponse() {
        val launch = launchFragmentInContainer<LoginScreenFragment>()
        launch.onFragment {

        }
        assert(true)
    }
}