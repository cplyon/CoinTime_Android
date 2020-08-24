package ca.cplyon.cointime.ui.main

import androidx.lifecycle.*
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.Result.Success
import ca.cplyon.cointime.data.source.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoinViewModel(private val repository: CoinRepository ) : ViewModel() {

    private var _items: LiveData<List<Coin>> = repository.observeCoins().switchMap { filterTasks(it) }
    val items: LiveData<List<Coin>> = _items

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun addCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.addCoin(coin)
    }


private fun filterTasks(coinResult: Result<List<Coin>>): LiveData<List<Coin>> {
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