package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import com.google.gson.annotations.SerializedName

class PlusCode {
    @SerializedName("compound_code")
    private lateinit var compoundCode: String

    @SerializedName("global_code")
    private lateinit var globalCode: String
}