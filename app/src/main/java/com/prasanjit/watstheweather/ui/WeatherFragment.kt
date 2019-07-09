package com.prasanjit.watstheweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.prasanjit.watstheweather.R
import com.prasanjit.watstheweather.utilities.Utilities
import com.prasanjit.watstheweather.viewmodel.LocationListenerViewModel
import com.prasanjit.watstheweather.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_fragment.*

/**
 * Weather fragment for displaying weather info
 */
class WeatherFragment : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /*
        everything inside companion object is static
        there should be only one companion object per class
     */
    companion object {
        private val TAG: String = WeatherFragment::class.java.simpleName
        fun newInstance() = WeatherFragment()
    }

    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationListenerViewModel: LocationListenerViewModel? = null
    private var weatherViewModel: WeatherViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        connectToGoogleApiClient()

        locationListenerViewModel = ViewModelProviders.of(this).get(LocationListenerViewModel::class.java)

        locationListenerViewModel?.currentLocation?.observe(this, Observer {
            Log.d(TAG, "LiveData Location: Latitude " + it?.latitude + " and Longitude " + it?.longitude)
            // Toast.makeText(this, "Latitude: " + it?.latitude + " Longitude: " + it?.longitude, Toast.LENGTH_SHORT).show()
            updateWeatherByCity("Delhi")
            // updateWeatherByCoordinates(it?.latitude ?: 0.0, it?.longitude ?: 0.0)

            stopLocationUpdates()
        })
    }

    // connection callbacks -------

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if ((!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) && Build.VERSION.SDK_INT >= 23) {
            val permissions =
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this.activity!!, permissions, 1000)
        } else {
            val location = LocationServices.getFusedLocationProviderClient(this.activity!!)

            location.requestLocationUpdates(locationRequest, object : LocationCallback() {

                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    p0?.lastLocation?.let {
                        Log.d(TAG, "Location received")
                        it.let { locationListenerViewModel?.updateLocation(it) }
                        Toast.makeText(activity, "onConnection", Toast.LENGTH_SHORT).show()
                        Log.d(
                            TAG,
                            "onConnected location: Latitude " + p0.lastLocation.latitude + " and Longitude " + p0.lastLocation.longitude
                        )
                        // updateWeatherByCoordinates(p0?.lastLocation.latitude ?: 0.0, p0?.lastLocation.longitude ?: 0.0)
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
        Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show()
    }

    // location listener

    override fun onLocationChanged(p0: Location?) {
        p0?.let { locationListenerViewModel?.updateLocation(p0) }
        // stop getting updates once received
        stopLocationUpdates()
        Toast.makeText(activity, "Location changed", Toast.LENGTH_SHORT).show()
    }

    private fun connectToGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(300000)
            .setFastestInterval(60000)
    }

    /**
     * check for permission
     */
    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectToGoogleApiClient()
                } else {
                    Toast.makeText(activity, "Oops! Permission denied for Location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
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

    /*
        observe for weather data
     */
    private fun updateWeatherByCity(city: String){
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        // weatherViewModel?.updateWeather("Thane")
        weatherViewModel?.updateWeather(city)

        weatherViewModel?.weatherData?.observe(this, Observer {

            val weather = it?.weather?.get(0)

            Log.d(TAG, "Weather name " + it?.name)
            Log.d(TAG, "Weather type " + weather?.description)
            Log.d(TAG, "Weather temperature " + it?.main?.temp)

            updateWeatherOnUI(weather?.id, it?.name, weather?.description, it?.main?.temp, it?.main?.humidity)

            if(it == null){
                Log.d(TAG, "It is null")
            } else weatherViewModel?.insert(it)
        })
    }

    /*
        observe for weather data
     */
    private fun updateWeatherByCoordinates(lat: Double, lon: Double){
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        // weatherViewModel?.updateWeather("Thane")
        weatherViewModel?.updateWeatherByCoordinates(lat, lon)

        weatherViewModel?.weatherData?.observe(this, Observer {

            val weather = it?.weather?.get(0)

            Log.d(TAG, "Weather name " + it?.name)
            Log.d(TAG, "Weather type " + weather?.description)
            Log.d(TAG, "Weather temperature " + Utilities.convertTempToDegrees(it?.main?.temp))

            updateWeatherOnUI(weather?.id, it?.name, weather?.description, it?.main?.temp, it?.main?.humidity)
        })
    }

    // updates the UI
    private fun updateWeatherOnUI(condition: Int?, name: String?, description: String?, temperature: Double?, humidity: Int?){
        when(condition){
            in 0..300 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.tstorm1))
            in 301..500 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.light_rain))
            in 501..600 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.shower3))
            in 601..700 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.snow4))
            in 701..771 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.fog))
            in 772..799 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.tstorm3))
            800 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.sunny))
            in 801..804 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.cloudy2))
            in 900..903 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.tstorm3))
            in 905..1000 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.tstorm3))
            983 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.snow5))
            904 -> imvCondition.setImageDrawable(activity?.getDrawable(R.drawable.sunny))
            else -> Log.d(TAG, "Wrong condition")
        }

        when(Utilities.getCurrentTime()){
            0 -> frameLayoutBackground.setBackgroundResource(R.drawable.blue_gradient_background)
            1 -> frameLayoutBackground.setBackgroundResource(R.drawable.orange_gradient_background)
            2 -> frameLayoutBackground.setBackgroundResource(R.drawable.dark_gradient_background)
        }

        txtPlace.text = name ?: "Place not available"
        txtCondition.text = description ?: "Condition not available"
        txtTemperature.text = Utilities.convertTempToDegrees(temperature).toString() + "\u00B0C"
        txtHumidity.text = humidity.toString() + "%"
    }

    fun setTextAndBackgroundColorForDay(){
        frameLayoutBackground.setBackgroundResource(R.drawable.blue_gradient_background)
    }

}
