package ca.cplyon.cointime

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return
        setupActionBarWithNavController(host.navController)
    }

    override fun onSupportNavigateUp(): Boolean {

        // hide the keyboard
        currentFocus?.let {
            val imm = ContextCompat.getSystemService(
                applicationContext,
                InputMethodManager::class.java
            )
            imm?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
