package ca.cplyon.cointime.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.FakeCoinRepositoryInstrumentation
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinListViewModelInstrumentationTest {

    private lateinit var fakeRepository: FakeCoinRepositoryInstrumentation
    private lateinit var vm: CoinListViewModel
    private val coins = listOf(Coin("Dummy", "Dummy", 0, "Dummy", "Dummy"))

    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepositoryInstrumentation(coins)
    }

    @Test
    fun loadImage_success() {
        fakeRepository.returnError = false
        vm = CoinListViewModel(fakeRepository)
        Assert.assertNotNull(vm.loadImage("fake_path"))
    }
}
