package ca.cplyon.cointime.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.cplyon.cointime.FakeCoinTimeApplication
import ca.cplyon.cointime.MainCoroutineRule
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.FakeCoinRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CoinDetailViewModelUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeRepository: FakeCoinRepository
    private lateinit var vm: CoinDetailViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val coins = listOf(Coin("Dummy", "Dummy", 0, "Dummy", "Dummy"))

    @ExperimentalCoroutinesApi
    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepository(coins)
        vm = CoinDetailViewModel(fakeRepository, FakeCoinTimeApplication())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addCoin_verify_lastCoinId() {
        val c = Coin("C", "D", 0, "MM", "N")
        c.coinId = 191919

        // There's got to be a better way!
        runBlockingTest {
            vm.addCoin(c)
        }

        val id = vm.lastCoinId
        assertThat(id, `is`(191919))
    }
}