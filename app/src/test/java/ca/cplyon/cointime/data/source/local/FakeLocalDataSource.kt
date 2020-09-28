package ca.cplyon.cointime.data.source.local

import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.source.CoinDataSource

class FakeLocalDataSource : CoinDataSource {

    var coinId = 1L

    override suspend fun getAllCoins(): Result<List<Coin>> {
        TODO("Not yet implemented")
    }

    override suspend fun addCoin(coin: Coin): Long {
        return coinId++
    }

    override suspend fun deleteCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {
        TODO("Not yet implemented")
    }

    fun setNextCoinId(id: Long) {
        coinId = id
    }

}