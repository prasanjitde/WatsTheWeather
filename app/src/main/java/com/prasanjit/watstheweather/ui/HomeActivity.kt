package com.prasanjit.watstheweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.prasanjit.watstheweather.R
import com.prasanjit.watstheweather.utilities.Utilities
import com.prasanjit.watstheweather.viewmodel.LocationListenerViewModel
import com.prasanjit.watstheweather.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.content_main.*

class HomeActivity : FragmentActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationListenerViewModel: LocationListenerViewModel? = null
    private var weatherViewModel: WeatherViewModel? = null

    companion object {
        private val TAG: String = HomeActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectToGoogleApiClient()

        locationListenerViewModel = ViewModelProviders.of(this).get(LocationListenerViewModel::class.java)

        locationListenerViewModel?.currentLocation?.observe(this, Observer {
            Log.d(TAG, "Current location: Latitude " + it?.latitude + " and Longitude " + it?.longitude)
            Toast.makeText(this, "Latitude: " + it?.latitude + " Longitude: " + it?.longitude, Toast.LENGTH_SHORT).show()
            updateWeatherByCity("Darjeeling,India")
            // updateWeatherByCoordinates(it?.latitude ?: 0.0, it?.longitude ?: 0.0)
        })
    }

    private fun updateWeatherByCity(city: String){
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        // weatherViewModel?.updateWeather("Thane")
        weatherViewModel?.updateWeatherByCity(city)

        weatherViewModel?.weatherData?.observe(this, Observer {

            val weather = it?.weather?.get(0)

            Log.d(TAG, "Weather name " + it?.name)
            Log.d(TAG, "Weather type " + weather?.description)
            Log.d(TAG, "Weather temperature " + it?.main?.temp)

            updateWeatherOnUI(weather?.id, it?.name, weather?.description, it?.main?.temp)
        })
    }

    private fun updateWeatherByCoordinates(lat: Double, lon: Double){
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        // weatherViewModel?.updateWeather("Thane")
        weatherViewModel?.updateWeatherByCoordinates(lat, lon)

        weatherViewModel?.weatherData?.observe(this, Observer {

            val weather = it?.weather?.get(0)

            Log.d(TAG, "Weather name " + it?.name)
            Log.d(TAG, "Weather type " + weather?.description)
            Log.d(TAG, "Weather temperature " + Utilities.convertTempToDegrees(it?.main?.temp))

            updateWeatherOnUI(weather?.id, it?.name, weather?.description, it?.main?.temp)
        })
    }

    private fun updateWeatherOnUI(condition: Int?, name: String?, description: String?, temperature: Double?){

        when(condition){
            in 0..300 -> imvCondition.setImageDrawable(getDrawable(R.drawable.tstorm1))
            in 301..500 -> imvCondition.setImageDrawable(getDrawable(R.drawable.light_rain))
            in 501..600 -> imvCondition.setImageDrawable(getDrawable(R.drawable.shower3))
            in 601..700 -> imvCondition.setImageDrawable(getDrawable(R.drawable.snow4))
            in 701..771 -> imvCondition.setImageDrawable(getDrawable(R.drawable.fog))
            in 772..799 -> imvCondition.setImageDrawable(getDrawable(R.drawable.tstorm3))
            800 -> imvCondition.setImageDrawable(getDrawable(R.drawable.sunny))
            in 801..804 -> imvCondition.setImageDrawable(getDrawable(R.drawable.cloudy2))
            in 900..903 -> imvCondition.setImageDrawable(getDrawable(R.drawable.tstorm3))
            in 905..1000 -> imvCondition.setImageDrawable(getDrawable(R.drawable.tstorm3))
            983 -> imvCondition.setImageDrawable(getDrawable(R.drawable.snow5))
            904 -> imvCondition.setImageDrawable(getDrawable(R.drawable.sunny))
            else -> Log.d(TAG, "Wrong condition")
        }

        txtPlace.text = name ?: "Place not available"
        txtCondition.text = description ?: "Condition not available"
        txtTemperature.text = Utilities.convertTempToDegrees(temperature).toString() + "\u00B0C"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // connection callbacks -------

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if ((!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) && Build.VERSION.SDK_INT >= 23) {
            val permissions =
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, 1000)
        } else {
            val location = LocationServices.getFusedLocationProviderClient(this)

            location.requestLocationUpdates(locationRequest, object : LocationCallback() {

                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    p0?.lastLocation?.let {
                        Log.d(TAG, "Location received")
                        it.let { locationListenerViewModel?.updateLocation(it) }
                    }
                    // stop after getting the loc once
                    location.removeLocationUpdates(this)
                    stopLocationUpdates()
                }
            }, null)

            location.lastLocation.addOnSuccessListener {
                Log.d(TAG, "Got last known location ")
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {}

    // on connection failed listener --------

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed")
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
    }


    // location listener

    override fun onLocationChanged(p0: Location?) {
        p0?.let { locationListenerViewModel?.updateLocation(p0) }
        // stop getting updates once received
        stopLocationUpdates()
        Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show()
    }

    private fun connectToGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000)
            .setFastestInterval(1000)
    }

    /**
     * check for permission
     */
    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectToGoogleApiClient()
                } else {
                    Toast.makeText(this, "Cannot get Location Updates", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        googleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        googleApiClient?.isConnected?.let { if (it) googleApiClient?.disconnect() }

    }
}
