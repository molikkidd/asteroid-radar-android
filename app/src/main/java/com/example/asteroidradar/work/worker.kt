package com.example.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.repository.AsteroidsRepository
import com.example.asteroidradar.database.getDatabase
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object{
       const val WORK_NAME = "AsteroidWorker"
    }
    override suspend fun doWork(): Result {

        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}