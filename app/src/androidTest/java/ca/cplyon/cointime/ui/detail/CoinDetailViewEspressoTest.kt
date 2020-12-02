package ca.cplyon.cointime.ui.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CoinDetailViewEspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun new_coin_obverse_exists_test() {
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to null),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.obverse)).check(matches(isDisplayed()))
    }

    @Test
    fun new_coin_reverse_exists_test() {
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to null),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.reverse)).check(matches(isDisplayed()))
    }

    @Test
    fun new_coin_text_fields_test() {
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to null),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.coinCountry)).check(matches(withText("")))
        onView(withId(R.id.coinDenomination)).check(matches(withText("")))
        onView(withId(R.id.coinYear)).check(matches(withText("")))
        onView(withId(R.id.coinMintMark)).check(matches(withText("")))
        onView(withId(R.id.coinNotes)).check(matches(withText("")))
    }

    @Test
    fun edit_coin_text_fields_test() {
        val coin = Coin("USA", "25 cents", 2020, "D", "Quarter")
        launchFragmentInContainer<CoinDetailFragment>(
            fragmentArgs = bundleOf("coin" to coin),
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.coinCountry)).check(matches(withText(coin.country)))
        onView(withId(R.id.coinDenomination)).check(matches(withText(coin.denomination)))
        onView(withId(R.id.coinYear)).check(matches(withText(coin.year.toString())))
        onView(withId(R.id.coinMintMark)).check(matches(withText(coin.mintMark)))
        onView(withId(R.id.coinNotes)).check(matches(withText(coin.notes)))
    }
}
