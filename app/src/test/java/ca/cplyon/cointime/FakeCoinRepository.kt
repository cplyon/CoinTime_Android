package ca.cplyon.cointime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.source.CoinRepository

class FakeCoinRepository : CoinRepository {
    override suspend fun getAllCoins(): Result<List<Coin>> {
        TODO("Not yet implemented")
    }

    override suspend fun addCoin(coin: Coin): Long {
        return coin.coinId
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {
        return MutableLiveData()
    }

    override suspend fun deleteCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCoin(coin: Coin) {
        TODO("Not yet implemented")
    }
}