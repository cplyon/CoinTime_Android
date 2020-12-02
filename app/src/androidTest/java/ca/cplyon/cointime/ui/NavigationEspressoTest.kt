package ca.cplyon.cointime.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.cplyon.cointime.R
import ca.cplyon.cointime.ui.main.CoinListFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationEspressoTest {

    @Test
    fun navigate_list_to_detail_test() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_coin)
        val coinListScenario = launchFragmentInContainer<CoinListFragment>(
            themeResId = R.style.AppTheme
        )
        coinListScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(ViewMatchers.withId(R.id.add_coin_fab)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.detail_dest)
    }

    @Test
    fun navigate_back_to_list_test() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.nav_coin)
        val coinListScenario = launchFragmentInContainer<CoinListFragment>(
            themeResId = R.style.AppTheme
        )
        coinListScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(ViewMatchers.withId(R.id.add_coin_fab)).perform(click())
        navController.popBackStack()
        assertEquals(navController.currentDestination?.id, R.id.list_dest)
    }
}
