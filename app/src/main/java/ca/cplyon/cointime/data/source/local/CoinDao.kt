package ca.cplyon.cointime.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.cplyon.cointime.data.Coin

@Dao
interface CoinDao {

    @Query("SELECT * from coin_table")
    suspend fun getAllCoins(): List<Coin>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCoin(coin: Coin): Long

    @Query("SELECT * FROM coin_table")
    fun observeCoins(): LiveData<List<Coin>>

    @Delete
    suspend fun deleteCoin(coin: Coin)

    @Update
    suspend fun updateCoin(coin: Coin)
}
