package ca.cplyon.cointime.ui.detail

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.cplyon.cointime.FakeCoinTimeApplicationInstrumentation
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.data.source.FakeCoinRepositoryInstrumentation
import org.hamcrest.core.StringEndsWith
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoinDetailViewModelInstrumentationTest {

    private lateinit var fakeRepository: FakeCoinRepositoryInstrumentation
    private lateinit var vm: CoinDetailViewModel
    private val coins = listOf(Coin("Dummy", "Dummy", 0, "Dummy", "Dummy"))
    private val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    @Before
    fun setUpViewModel() {
        fakeRepository = FakeCoinRepositoryInstrumentation(coins)
    }

    @Test
    fun saveObverseImage_failure() {
        fakeRepository.returnError = true
        vm = CoinDetailViewModel(fakeRepository, FakeCoinTimeApplicationInstrumentation())
        val fileName = vm.saveObverseImage(bitmap)
        Assert.assertNull(fileName)
    }

    @Test
    fun saveReverseImage_failure() {
        fakeRepository.returnError = true
        vm = CoinDetailViewModel(fakeRepository, FakeCoinTimeApplicationInstrumentation())
        val fileName = vm.saveReverseImage(bitmap)
        Assert.assertNull(fileName)
    }

    @Test
    fun saveObverseImage_success() {
        fakeRepository.returnError = false
        vm = CoinDetailViewModel(fakeRepository, FakeCoinTimeApplicationInstrumentation())
        val fileName = vm.saveObverseImage(bitmap)
        Assert.assertThat(fileName, StringEndsWith("_obverse.png"))
    }

    @Test
    fun saveReverseImage_success() {
        fakeRepository.returnError = false
        vm = CoinDetailViewModel(fakeRepository, FakeCoinTimeApplicationInstrumentation())
        val fileName = vm.saveReverseImage(bitmap)
        Assert.assertThat(fileName, StringEndsWith("_reverse.png"))
    }
}