package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import com.google.gson.annotations.SerializedName

class Photo {
    var height: Int = 0
    @SerializedName("html_attributions")
    lateinit var htmlAttributions: Array<String>
    @SerializedName("photo_reference")
    lateinit var photoReference: String
    var width: Int = 0
}