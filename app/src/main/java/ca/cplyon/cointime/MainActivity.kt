package ca.cplyon.cointime

import android.os.Bundle
import android.view.MenuItem
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}