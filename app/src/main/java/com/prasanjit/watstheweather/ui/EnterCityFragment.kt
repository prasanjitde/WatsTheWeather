package com.prasanjit.watstheweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prasanjit.watstheweather.R
import com.prasanjit.watstheweather.viewmodel.WeatherViewModel

class EnterCityFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel

    companion object{
        fun newInstance() = EnterCityFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_city, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edtEnterCity.setOnEditorActionListener{ v, keyCode, event ->
            when(keyCode){
                EditorInfo.IME_ACTION_SEARCH -> {
                    sendSearch()
                    true
                }
                else -> false
            }
        }
    }*/

    fun sendSearch(){

    }
}
