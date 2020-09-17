package ca.cplyon.cointime

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.ui.main.CoinViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinViewModelUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeRepository: FakeCoinRepository
    private lateinit var vm: CoinViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val coins = listOf(Coin("Dummy", "Dummy", 0, "Dummy", "Dummy"))

    @ExperimentalCoroutinesApi
    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepository(coins)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addCoin_verify_lastCoinId() {

        vm = CoinViewModel(fakeRepository, FakeCoinTimeApplication())

        val observer = Observer<List<Coin>> {}

        val c = Coin("C", "D", 0, "MM", "N")
        c.coinId = 191919

        try {
            vm.items.observeForever(observer)
            // There's got to be a better way!
            runBlockingTest {
                vm.addCoin(c).join()
            }

            val id = vm.lastCoinId
            assertThat(id, `is`(191919))
        } finally {
            vm.items.removeObserver(observer)
        }

    }

    @Test
    fun filterCoins_success() {
        val observer = Observer<List<Coin>> {}
        vm = CoinViewModel(fakeRepository, FakeCoinTimeApplication())
        try {
            vm.items.observeForever(observer)
            Assert.assertEquals(vm.items.value, coins)
        } finally {
            vm.items.removeObserver(observer)
        }
    }


    @Test
    fun filterCoins_failure() {
        val observer = Observer<List<Coin>> {}
        fakeRepository.setReturnError(true)
        vm = CoinViewModel(fakeRepository, FakeCoinTimeApplication())
        try {
            vm.items.observeForever(observer)
            Assert.assertEquals(vm.items.value, emptyList<Coin>())
        } finally {
            vm.items.removeObserver(observer)
        }
    }

}