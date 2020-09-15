package ca.cplyon.cointime.data.source

import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result

interface CoinRepository {
    suspend fun getAllCoins(): Result<List<Coin>>

    suspend fun addCoin(coin: Coin): Long

    fun observeCoins(): LiveData<Result<List<Coin>>>

    suspend fun deleteCoin(coin: Coin)

    suspend fun updateCoin(coin: Coin)
}