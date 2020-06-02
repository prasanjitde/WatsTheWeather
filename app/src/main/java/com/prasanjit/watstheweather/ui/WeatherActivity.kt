package com.prasanjit.watstheweather.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.prasanjit.watstheweather.R
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.app_bar_weather.*
import kotlinx.android.synthetic.main.content_weather.*

/**
 * Weather screen
 */
class WeatherActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        loadWeatherFragment()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun loadWeatherFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, WeatherFragment.newInstance(), "weather_fragment").commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_city -> {
                openAddCityFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAddCityFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.frameLayout, EnterCityFragment.newInstance(), "add_city_fragment").commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_weather -> {

            }
            R.id.nav_share -> {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
