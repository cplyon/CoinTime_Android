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
        coin.obverse?.let {
            deleteImage(it)
        }

        coin.reverse?.let {
            deleteImage(it)
        }
    }

    override fun deleteImage(path: String) {
        val f = File(path)
        if (f.exists()) {
            f.delete()
        }
    }

    override suspend fun updateCoin(coin: Coin) {
        localDataSource.updateCoin(coin)
    }

    override fun saveImage(context: Context, image: Bitmap, suffix: String): String? {
        val path = generateImagePath(context, suffix)
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

    override fun loadImage(path: String): Bitmap? {
        val f = File(path)
        if (f.exists()) {
            return BitmapFactory.decodeFile(path)
        }
        return null
    }

    private fun generateImagePath(context: Context, suffix: String): Path {
        val storageDir = context.getDir("coin_images", Context.MODE_PRIVATE)
        val filename =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + "_$suffix.png"
        return Paths.get(storageDir.toString(), filename)
    }

}