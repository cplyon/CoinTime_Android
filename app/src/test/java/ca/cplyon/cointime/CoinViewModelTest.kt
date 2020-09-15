package ca.cplyon.cointime

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.ui.main.CoinViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
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


    @ExperimentalCoroutinesApi
    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepository()
        vm = CoinViewModel(fakeRepository)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addCoin_verify_lastCoinId() {

        val observer = Observer<List<Coin>> {}

        val c = Coin("C", "D", 0, "MM", "N")
        c.coinId = 191919

        vm.items.observeForever(observer)

        // There's got to be a better way!
        runBlockingTest {
            vm.addCoin(c)
        }

        val id = vm.lastCoinId
        assertThat(id, `is`(191919))

    }

}