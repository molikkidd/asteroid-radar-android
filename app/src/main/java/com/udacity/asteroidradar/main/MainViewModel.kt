package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
//import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainViewModel : ViewModel() {

//    Encapsulate the asteroid list data
    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList


    /**
     * Call getAsteroidData() on init so we can display status immediately.
     */
    init {
        getAsteroidData()
    }

    val start_date = "2021-12-07"
    val end_date = "2021-12-19"

//    grab asteroids from the api and attach to live data variable list
    private fun getAsteroidData() {
        viewModelScope.launch {
            try {
//                pass in current date
               val rowListResult = AsteroidsApi.retrofitService.getAsteroids(start_date,end_date)
                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(rowListResult)).toList()
//                _asteroidList.value = asteroidList.value

            } catch (e: Exception) {
                val rowResult = "Failure: " + e.message
                _asteroidList.value = listOf()

            }


        }
    }
}