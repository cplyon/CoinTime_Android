package ca.cplyon.cointime

import android.content.Context
import ca.cplyon.cointime.data.source.CoinRepository
import ca.cplyon.cointime.data.source.DefaultCoinRepository
import ca.cplyon.cointime.data.source.local.CoinLocalDataSource
import ca.cplyon.cointime.data.source.local.CoinRoomDatabase

object ServiceLocator {

    private var database: CoinRoomDatabase? = null
    private var coinRepository: CoinRepository? = null

    fun provideCoinRepository(context: Context): CoinRepository {
        return coinRepository ?: createCoinRepository(context)
    }

    private fun createCoinRepository(context: Context): CoinRepository {
        val newRepository = DefaultCoinRepository(createCoinLocalDataSource(context))
        coinRepository = newRepository
        return newRepository
    }

    private fun createCoinLocalDataSource(context: Context): CoinLocalDataSource {
        val db = database ?: createDatabase(context)
        return CoinLocalDataSource(db.coinDao())
    }

    private fun createDatabase(context: Context): CoinRoomDatabase {
        val result = CoinRoomDatabase.getDatabase(context)
        database = result
        return result
    }
}
