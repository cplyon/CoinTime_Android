package ca.cplyon.cointime.data.source

import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result

interface CoinDataSource {

    suspend fun getAllCoins(): Result<List<Coin>>
    suspend fun addCoin(coin: Coin)

    fun observeCoins(): LiveData<Result<List<Coin>>>
}