package com.example.workbench_240723

import GoogleSheetsService
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.workbench_240723.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetsService: GoogleSheetsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Usage
        lifecycleScope.launch {
            if (testInternetConnection()) {
//                println("Internet connection successful")
                Log.d("NetworkCheck", "connection successful")
            } else {
                Log.d("NetworkCheck", "connection failed")
                println("Internet connection failed")
            }
        }

        val credentialsStream = resources.openRawResource(R.raw.gg_credentials)
        sheetsService = GoogleSheetsService(credentialsStream)

        // Use the service to update a cell
        // TODO get value from cell
        Thread {
            try {
//                sheetsService.updateCell(
//                    "1Y8tjOSrSlB19KNUSckgrZZdfv4bwv63L2QjnkzAU1EY",
//                    "Sheet1!A1",
//                    "Hello, World!"
//                )

                var cellValue = sheetsService.getCell("1Y8tjOSrSlB19KNUSckgrZZdfv4bwv63L2QjnkzAU1EY", "Sheet1!D1")
                Log.d("GoogleSheet getCell", cellValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_scrolling
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private suspend fun testInternetConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://www.google.com")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.connect()
                connection.responseCode == 200
            } catch (e: Exception) {
                false
            }
        }
    }
}