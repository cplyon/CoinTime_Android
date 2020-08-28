package ca.cplyon.cointime

import android.app.Application
import androidx.room.Room
import ca.cplyon.cointime.data.source.CoinRepository
import ca.cplyon.cointime.data.source.local.CoinLocalDataSource
import ca.cplyon.cointime.data.source.local.CoinRoomDatabase

class CoinTimeApplication : Application() {

    val coinRepository: CoinRepository
        get() = ServiceLocator.provideCoinRepository(this)

    override fun onCreate() {
        super.onCreate()

    }
}