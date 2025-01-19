package de.hdm_stuttgart.myfavlocation.backend.places_api

import de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places.PlacesAPIResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PlacesAPI {
    @GET
    fun call(@Url url: String): Call<PlacesAPIResult>
}

