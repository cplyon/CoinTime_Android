package ca.cplyon.cointime.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.Result.Error
import ca.cplyon.cointime.data.Result.Success
import ca.cplyon.cointime.data.source.CoinDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinLocalDataSource internal constructor(
    private val coinDao: CoinDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoinDataSource {

    override suspend fun getAllCoins(): Result<List<Coin>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(coinDao.getAllCoins())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun addCoin(coin: Coin) = withContext(ioDispatcher) {
        coinDao.addCoin(coin)
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {
        return coinDao.observeCoins().map {
            Success(it)
        }
    }

    override suspend fun deleteCoin(coin: Coin) = withContext(ioDispatcher) {
        coinDao.deleteCoin(coin)
    }

}