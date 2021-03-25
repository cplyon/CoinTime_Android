package ca.cplyon.cointime.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.Result.Success
import ca.cplyon.cointime.data.source.CoinRepository
import ca.cplyon.cointime.data.succeeded
import kotlinx.coroutines.launch

class CoinListViewModel(private val repository: CoinRepository) : ViewModel() {

    private var _items: LiveData<List<Coin>> =
        repository.observeCoins().switchMap { filterCoins(it) }
    val items: LiveData<List<Coin>> = _items

    @SuppressLint("NullSafeMutableLiveData")
    private fun filterCoins(coinResult: Result<List<Coin>>): LiveData<List<Coin>> {
        val result = MutableLiveData<List<Coin>>()
        if (coinResult.succeeded) {
            viewModelScope.launch {
                result.value = (coinResult as Success).data
            }
        } else {
            result.value = emptyList()
        }

        return result
    }

    fun loadImage(path: String): Bitmap? {
        return repository.loadImage(path)
    }
}

@Suppress("UNCHECKED_CAST")
class CoinListViewModelFactory(
    private val coinRepository: CoinRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CoinListViewModel(coinRepository) as T)
}
