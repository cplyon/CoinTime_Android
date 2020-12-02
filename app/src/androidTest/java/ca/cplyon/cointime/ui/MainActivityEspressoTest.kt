package ca.cplyon.cointime.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityEspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun search_menu_item_exists_test() {
        // ensure the search menu is present on the list view
        onView(withId(R.id.action_search)).check(matches(isDisplayed()))
    }

    @Test
    fun save_menu_item_exists_test() {
        // open the detail view by triggering the FAB (new coin)
        // ensure the save menu item is present and enabled
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.action_save)).check(matches(isDisplayed()))
        onView(withId(R.id.action_save)).check(matches(isEnabled()))
    }

    @Test
    fun edit_menu_item_exists_test() {
        // open the detail view by triggering the FAB (new coin)
        // ensure the edit menu item is not present
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.action_edit)).check(doesNotExist())
    }

    @Test
    fun delete_menu_item_exists_test() {
        // open the detail view by triggering the FAB (new coin)
        // ensure the delete menu item is present and enabled
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.action_delete)).check(matches(isDisplayed()))
        onView(withId(R.id.action_delete)).check(matches(not(isEnabled())))
    }
}
