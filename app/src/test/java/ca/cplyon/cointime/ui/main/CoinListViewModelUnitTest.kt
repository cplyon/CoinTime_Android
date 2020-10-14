package ca.cplyon.cointime.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import ca.cplyon.cointime.MainCoroutineRule
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.FakeCoinRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinListViewModelUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeRepository: FakeCoinRepository
    private lateinit var vm: CoinListViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val coins = listOf(Coin("Dummy", "Dummy", 0, "Dummy", "Dummy"))

    @ExperimentalCoroutinesApi
    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepository(coins)
    }

    @Test
    fun filterCoins_success() {
        fakeRepository.setReturnError(false)
        val observer = Observer<List<Coin>> {}
        vm = CoinListViewModel(fakeRepository)
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
        vm = CoinListViewModel(fakeRepository)
        try {
            vm.items.observeForever(observer)
            Assert.assertEquals(vm.items.value, emptyList<Coin>())
        } finally {
            vm.items.removeObserver(observer)
        }
    }

    @Test
    fun loadImage_failure() {
        fakeRepository.setReturnError(true)
        vm = CoinListViewModel(fakeRepository)
        Assert.assertNull(vm.loadImage("fake_path"))
    }

    @Test
    fun factory_success() {
        fakeRepository.setReturnError(false)
        val observer = Observer<List<Coin>> {}
        val vm = CoinListViewModelFactory(fakeRepository).create(CoinListViewModel::class.java)
        Assert.assertNotNull(vm)
        try {
            vm.items.observeForever(observer)
            Assert.assertEquals(vm.items.value, coins)
        } finally {
            vm.items.removeObserver(observer)
        }
    }
}
