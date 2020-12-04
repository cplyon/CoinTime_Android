package ca.cplyon.cointime.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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

    @Test
    fun verify_new_coin_added_test() {
        val country = "ESPRESSO"
        val denomination = "25 cents"
        val year = "2020"
        val mintMark = "D"
        val notes = "quarter"

        // on list view
        onView(withId(R.id.add_coin_fab)).perform(click())

        // on detail view
        onView(withId(R.id.coinCountry)).perform(typeText(country))
        onView(withId(R.id.coinDenomination)).perform(typeText(denomination))
        onView(withId(R.id.coinYear)).perform(typeText(year))
        onView(withId(R.id.coinMintMark)).perform(typeText(mintMark))
        onView(withId(R.id.coinNotes)).perform(typeText(notes))
        onView(withId(R.id.action_save)).perform(click())

        // on list view
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(country))))
        onView(withId(R.id.recyclerview)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(country)),
                click()
            )
        )

        // on detail view
        onView(withId(R.id.coinCountry)).check(matches(withText(country)))
    }

    // TODO figure out how to validate images are the same using Espresso
/*
    @Test
    fun obverse_camera_intent_launch_test() {
        Intents.init()
        val resultData = Intent()
        val drawable = ContextCompat.getDrawable(InstrumentationRegistry.getInstrumentation().targetContext, R.drawable.ic_add)!!
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        resultData.putExtra("data", bitmap)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.obverse)).perform(click())
        Intents.release()
    }

    @Test
    fun reverse_camera_intent_launch_test() {
        Intents.init()
        val resultData = Intent()
        val drawable = ContextCompat.getDrawable(InstrumentationRegistry.getInstrumentation().targetContext, R.drawable.ic_add)!!
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        resultData.putExtra("data", bitmap)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
        onView(withId(R.id.add_coin_fab)).perform(click())
        onView(withId(R.id.reverse)).perform(click())
        Intents.release()
    }
    */
}
