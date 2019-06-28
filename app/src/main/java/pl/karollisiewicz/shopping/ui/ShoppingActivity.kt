package pl.karollisiewicz.shopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_shopping.*
import pl.karollisiewicz.shopping.R

class ShoppingActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
        setSupportActionBar(toolbar)
        setupAppBarConfiguration()
        setupActionBarWithNavController(
            findNavController(R.id.navHostFragment),
            appBarConfiguration
        )
    }

    private fun setupAppBarConfiguration() {
        appBarConfiguration = AppBarConfiguration.Builder(R.id.shoppingListFragment).build()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
