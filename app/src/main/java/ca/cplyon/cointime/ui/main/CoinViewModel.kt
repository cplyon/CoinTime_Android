package ca.cplyon.cointime.ui.main

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.Result.Success
import ca.cplyon.cointime.data.source.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoinViewModel(private val repository: CoinRepository, application: Application) :
    AndroidViewModel(
        application
    ) {

    private var _items: LiveData<List<Coin>> =
        repository.observeCoins().switchMap { filterCoins(it) }
    val items: LiveData<List<Coin>> = _items

    // the id of the last coin added
    var lastCoinId: Long = 0

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun addCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        // it's possible there is a race condition here, since we're running addCoin in a coroutine,
        // addCoin may not return before lastCoinId is accessed by the main thread
        lastCoinId = repository.addCoin(coin)
    }

    fun deleteCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCoin(coin)
    }

    fun updateCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCoin(coin)
    }

    fun saveImage(image: Bitmap): String? {
        val context = this.getApplication<CoinTimeApplication>().applicationContext
        return repository.saveImage(context, image)
    }

    fun loadImage(path: String): Bitmap {
        return repository.loadImage(path)
    }

    private fun filterCoins(coinResult: Result<List<Coin>>): LiveData<List<Coin>> {
        val result = MutableLiveData<List<Coin>>()
        if (coinResult is Success) {
            viewModelScope.launch {
                result.value = coinResult.data
            }
        } else {
            result.value = emptyList()
        }

        return result
    }

}


@Suppress("UNCHECKED_CAST")
class CoinViewModelFactory(
    private val coinRepository: CoinRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CoinViewModel(coinRepository, application) as T)
}