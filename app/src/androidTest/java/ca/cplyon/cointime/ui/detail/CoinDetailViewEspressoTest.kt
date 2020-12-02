package ca.cplyon.cointime.ui.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
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
class CoinDetailViewEspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun obverse_exists_new_coin_test() {
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to null),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.obverse)).check(matches(isDisplayed()))
    }

    @Test
    fun reverse_exists_new_coin_test() {
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to null),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.reverse)).check(matches(isDisplayed()))
    }
}
