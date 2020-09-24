package ca.cplyon.cointime.ui.main

import androidx.lifecycle.*
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.Result
import ca.cplyon.cointime.data.Result.Success
import ca.cplyon.cointime.data.source.CoinRepository
import kotlinx.coroutines.launch

class CoinListViewModel(val repository: CoinRepository) : ViewModel() {

    private var _items: LiveData<List<Coin>> =
        repository.observeCoins().switchMap { filterCoins(it) }
    val items: LiveData<List<Coin>> = _items

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
class CoinListViewModelFactory(
    private val coinRepository: CoinRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CoinListViewModel(coinRepository) as T)
}