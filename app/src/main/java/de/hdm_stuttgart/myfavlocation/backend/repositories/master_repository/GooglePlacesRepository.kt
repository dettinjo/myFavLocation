package de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository

import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnAPIResponse
import de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places.PlacesAPIResult
import de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places.PlacesLocation
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.places_api.PlacesAPI
import de.hdm_stuttgart.myfavlocation.backend.data.BASE_URL
import de.hdm_stuttgart.myfavlocation.backend.data.PLACES_API_KEY
import de.hdm_stuttgart.myfavlocation.backend.data.RADIUS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class GooglePlacesRepository private constructor(
    private val gpsRepository: GPSRepository
){
    private var nearbyPlacesCache : MutableList<Location> = mutableListOf()

    /**
     *
     *
     */
    private fun prepareCall(url: String): Call<PlacesAPIResult> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val placesAPI = retrofit.create(PlacesAPI::class.java)
        return placesAPI.call(url)
    }

    /**
     * @return nearbyPlacesCache
     */
    fun getCache(): List<Location> {
        gpsRepository.calculateProximity(nearbyPlacesCache)
        return nearbyPlacesCache
    }

    /**
     * @param callback
     * @param searchInput
     *
     * generate a url for a textsearch in the google-places-api and call 'searchRequest'
     */
    fun searchRequestNamedSearch(callback: OnAPIResponse, searchInput: String) {
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=$searchInput&key=$PLACES_API_KEY"
        searchRequest(callback, url, false)
    }

    /**
     * @param callback
     *
     * generate a url for a nearbyPlaces-Search in the google-places-api and call 'searchRequest'
     */
    fun searchRequestNearbySearch(callback: OnAPIResponse) {
        val currentPosition = gpsRepository.getCurrentPosition()
        if (currentPosition != null) {
            val location = "location=${currentPosition.lat},${currentPosition.lng}&radius=$RADIUS"
            val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?$location&key=$PLACES_API_KEY"
            searchRequest(callback, url, true)
        }
    }

    /**
     * @param callback
     * @param url
     * @param searchType 0 = nearbySearch, 1 = namedSearch
     *
     * issue an asynchronous http-api-request. Once the result is returned, call the invoking ViewModel back
     */
    private fun searchRequest(callback : OnAPIResponse, url: String, searchType: Boolean) {
        val call: Call<PlacesAPIResult> = prepareCall(url)

        call.enqueue(object : Callback<PlacesAPIResult> {
            override fun onResponse(call: Call<PlacesAPIResult>, response: Response<PlacesAPIResult>) {
                if (!response.isSuccessful) {
                    callback.onFailure(response.message(), url)
                } else {
                    val allResults: MutableList<PlacesLocation> = mutableListOf()
                    val result: PlacesAPIResult? = response.body()
                    if (result != null) {
                        allResults.addAll(result.results)

                        createImageUrls(allResults)
                        val transformedResults = massTransform(allResults)
                        if(searchType){
                        nearbyPlacesCache = transformedResults
                        }
                        callback.onSuccess(transformedResults)
                    }
                }
            }

            override fun onFailure(call: Call<PlacesAPIResult>, t: Throwable) {
                callback.onFailure(t.message!!, url)
            }
        })
    }

    /**
     * @param initial
     * @return the transformed list
     *
     * transform a list of google-places-locations into a
     * list of regular locations (used elsewhere in the app and local db)
     * Also calculate the local proximity to the user
     */
    private fun massTransform(initial: List<PlacesLocation>): MutableList<Location> {
        val final: MutableList<Location> = mutableListOf()
        initial.forEach {
                e -> final.add(e.transform())
        }
        gpsRepository.calculateProximity(final)
        return final
    }

    /**
     * @param locations
     * generate urls for images by using the reference-codes from the google-places-api
     */
    private fun createImageUrls(locations : MutableList<PlacesLocation>){
        for (location in locations) {
            if(location.hasPhotos()){
                val urls : MutableList<String> = mutableListOf()
                location.photos.forEach {
                    val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${it.photoReference}&key=$PLACES_API_KEY"
                    urls.add(url)
                }
                location.photoURLs = urls
            }
        }
    }

    companion object {
        @Volatile private var instance : GooglePlacesRepository? = null
        fun getInstance(gpsRepository: GPSRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: GooglePlacesRepository(gpsRepository)
                            .also { instance = it }
                }
    }
}
