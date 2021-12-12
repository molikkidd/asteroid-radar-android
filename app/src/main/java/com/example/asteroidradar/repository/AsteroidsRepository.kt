package com.example.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.asteroidradar.Constants
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.network.*
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.database.asDomainModel
import com.example.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.ArrayList

class AsteroidsRepository(private val database: AsteroidDatabase) {

    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(startDate: String = getToday(), endDate: String = getSeventhDay()){
        withContext(Dispatchers.IO){
            var asteroidList: ArrayList<Asteroid>
            val response = Network.service.getAsteroids(startDate, endDate, Constants.API_KEY)
            val jsonObj = JSONObject(response)
            asteroidList= parseAsteroidsJsonResult(jsonObj)

            database.asteroidDao.insertAllAsteroids(*asteroidList.asDomainModel())
        }
    }

}