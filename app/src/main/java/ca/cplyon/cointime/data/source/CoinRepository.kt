package ca.cplyon.cointime.data.source

import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class CoinRepository(
    private val localDataSource: CoinDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getAllCoins(): Result<List<Coin>> {
        return localDataSource.getAllCoins()
    }

    suspend fun addCoin(coin: Coin) {
        localDataSource.addCoin(coin)
    }

    fun observeCoins(): LiveData<Result<List<Coin>>> {
        return localDataSource.observeCoins()
    }
}