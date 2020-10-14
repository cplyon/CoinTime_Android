package ca.cplyon.cointime.data.source

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result

class FakeCoinRepository(coins: List<Coin>) : CoinRepository {

    private var shouldReturnError = false
    private var result: Result<List<Coin>> = Result.Success(coins)
    private var bitmap: Bitmap? = null

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
        if (value) {
            result = Result.Error(Exception("Fake exception"))
            bitmap = null
        } else {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }

    override suspend fun getAllCoins(): Result<List<Coin>> {
        TODO("Not yet implemented")
    }

    override suspend fun addCoin(coin: Coin): Long {
        return coin.coinId
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {
        return MutableLiveData(result)
    }

    override suspend fun deleteCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override fun saveImage(context: Context, image: Bitmap, suffix: String): String? {
        TODO("Not yet implemented")
    }

    override fun loadImage(path: String): Bitmap? {
        return bitmap
    }

    override fun deleteImage(path: String) {
        TODO("Not yet implemented")
    }
}
