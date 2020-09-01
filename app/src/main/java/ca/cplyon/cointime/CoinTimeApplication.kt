package ca.cplyon.cointime

import android.app.Application
import ca.cplyon.cointime.data.source.CoinRepository

class CoinTimeApplication : Application() {

    val coinRepository: CoinRepository
        get() = ServiceLocator.provideCoinRepository(this)

}