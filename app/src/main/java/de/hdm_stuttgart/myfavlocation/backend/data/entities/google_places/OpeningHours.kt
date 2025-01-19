package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import com.google.gson.annotations.SerializedName

class OpeningHours {
    @SerializedName("open_now")
    private var openNow : Boolean = false
}