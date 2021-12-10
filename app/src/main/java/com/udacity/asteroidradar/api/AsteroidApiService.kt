package com.udacity.asteroidradar.api


// import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import retrofit2.Call
// import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = Constants.API_KEY

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface AsteroidApiService {
    /**
     * Returns a Coroutine [List] of [Asteroids] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "neo/rest/v1/feed" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date")
        startDate: String,
        @Query("end_date")
        endDate: String,
        @Query("api_key")
        key: String = API_KEY
    ):  String

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AsteroidsApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}
//