package ca.cplyon.cointime.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CoinListViewEspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun fab_launches_detail_fragment_test() {
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.detail)).check(matches(isDisplayed()))
    }

    @Test
    fun search_menu_item_exists_test() {
        onView(withId(R.id.action_search)).check(matches(isDisplayed()))
    }
}