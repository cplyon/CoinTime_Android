package ca.cplyon.cointime.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*


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
        if (coin.obverse != null) {
            File(coin.obverse!!).delete()
        }
        if (coin.reverse != null) {
            File(coin.reverse!!).delete()
        }
    }

    override suspend fun updateCoin(coin: Coin) {
        //TODO: delete old images if coin is updated
        localDataSource.updateCoin(coin)
    }

    override fun saveImage(context: Context, image: Bitmap): String? {
        val path = generateImagePath(context)
        try {
            FileOutputStream(path.toFile()).use { out ->
                image.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                )
            }
        } catch (e: IOException) {
            return null
        }
        return path.toString()
    }

    override fun loadImage(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    private fun generateImagePath(context: Context): Path {
        val storageDir = context.getDir("coin_images", Context.MODE_PRIVATE)
        val filename =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".png"
        return Paths.get(storageDir.toString(), filename)
    }

}