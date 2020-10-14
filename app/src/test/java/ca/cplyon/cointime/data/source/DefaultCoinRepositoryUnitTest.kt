package ca.cplyon.cointime.data.source

import ca.cplyon.cointime.MainCoroutineRule
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.local.FakeLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class DefaultCoinRepositoryUnitTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Test
    fun addCoin_verify_id() = mainCoroutineRule.runBlockingTest {
        val coin = Coin("country", "denomination", 2020, "MintMark", "Notes")
        val source = FakeLocalDataSource()
        source.setNextCoinId(191919)
        val repository: CoinRepository = DefaultCoinRepository(source)
        val id = repository.addCoin(coin)
        Assert.assertEquals(id, 191919)
    }
}
