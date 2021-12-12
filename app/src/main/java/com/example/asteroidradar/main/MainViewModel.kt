package com.example.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.asteroidradar.Constants
import com.example.asteroidradar.PictureOfDay
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.network.ImageApi
import com.example.asteroidradar.repository.AsteroidsRepository
import com.example.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

enum class ImageApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _image = MutableLiveData<PictureOfDay>()
    val image: LiveData<PictureOfDay>
        get() = _image

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    private val _status = MutableLiveData<ImageApiStatus>()
    val status: LiveData<ImageApiStatus>
        get() = _status

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()

            } catch (e: Exception){
                Log.e(e.message, "Error in getting asteroids from Cache")
            }
        }
        getImageOfTheDay()
    }

    val asteroids = repository.asteroids

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            _status.value = ImageApiStatus.LOADING
            try {
                var result = ImageApi.retrofitService.getImageOfTheDay(Constants.API_KEY)
                _image.value = result
                _status.value = ImageApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ImageApiStatus.ERROR
                _image.value = PictureOfDay("","","","","","","","")
            }
        }
    }

}