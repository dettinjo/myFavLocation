package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import com.google.gson.annotations.SerializedName

/**
 * The overarching response-object from the google-places-api
 */
class PlacesAPIResult {
    @SerializedName("html_attributions")
    private lateinit var htmlAttributions : Array<String>
    @SerializedName("next_page_token")
    lateinit var nextPageToken : String
    lateinit var results : Array<PlacesLocation>
    fun hasNext() : Boolean {
        return this::nextPageToken.isInitialized
    }
}