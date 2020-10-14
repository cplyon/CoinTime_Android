package ca.cplyon.cointime.data.source

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result

class FakeCoinRepositoryInstrumentation(private val coins: List<Coin>) : CoinRepository {

    var returnError = false

    override suspend fun getAllCoins(): Result<List<Coin>> {
        TODO("Not yet implemented")
    }

    override suspend fun addCoin(coin: Coin): Long {
        return coin.coinId
    }

    override fun observeCoins(): LiveData<Result<List<Coin>>> {

        var result: Result<List<Coin>> = Result.Success(coins)
        if (returnError) {
            result = Result.Error(Exception("Fake exception"))
        }
        return MutableLiveData(result)
    }

    override suspend fun deleteCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCoin(coin: Coin) {
        TODO("Not yet implemented")
    }

    override fun saveImage(context: Context, image: Bitmap, suffix: String): String? {
        if (returnError) {
            return null
        }
        return "fake_file_name_$suffix.png"
    }

    override fun loadImage(path: String): Bitmap? {
        if (returnError) {
            return null
        }
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    override fun deleteImage(path: String) {
        TODO("Not yet implemented")
    }
}
