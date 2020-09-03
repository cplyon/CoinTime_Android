package ca.cplyon.cointime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.MainActivityBinding
import ca.cplyon.cointime.ui.detail.CoinDetailFragment
import ca.cplyon.cointime.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    fun launchDetailFragment(coin: Coin?, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CoinDetailFragment.newInstance(coin), tag)
            .addToBackStack(null)
            .commit()
    }
}