package ca.cplyon.cointime.data.source

import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result


class DefaultCoinRepository(
    private val localDataSource: CoinDataSource
) : CoinRepository {

    override suspend fun getAllCoins(): Result<List<Coin>> {
        return localDataSource.getAllCoins()
    }

    override suspend fun addCoin(coin: Coin): Long {
        return localDataSource.addCoin(coin)
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {
        return localDataSource.observeCoins()
    }

    override suspend fun deleteCoin(coin: Coin) {
        localDataSource.deleteCoin(coin)
    }

    override suspend fun updateCoin(coin: Coin) {
        localDataSource.updateCoin(coin)
    }
}