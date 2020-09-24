package ca.cplyon.cointime.ui.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoinDetailViewModel(private val repository: CoinRepository, application: Application) :
    AndroidViewModel(
        application
    ) {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun addCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.addCoin(coin)
    }

    fun deleteCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCoin(coin)
    }

    fun updateCoin(coin: Coin) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCoin(coin)
    }

    fun saveObverseImage(image: Bitmap): String? {
        return repository.saveImage(getApplication(), image, "obverse")
    }

    fun saveReverseImage(image: Bitmap): String? {
        return repository.saveImage(getApplication(), image, "reverse")
    }

    fun loadImage(path: String): Bitmap? {
        return repository.loadImage(path)
    }

    fun deleteImage(path: String) {
        repository.deleteImage(path)
    }

}


@Suppress("UNCHECKED_CAST")
class CoinDetailViewModelFactory(
    private val coinRepository: CoinRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CoinDetailViewModel(coinRepository, application) as T)
}
