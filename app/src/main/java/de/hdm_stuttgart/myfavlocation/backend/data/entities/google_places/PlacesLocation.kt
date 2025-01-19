package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import com.google.gson.annotations.SerializedName
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.enumerations.Type
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position

/**
 * A single location-object.
 * Part of the response from the google-places-api
 */
class PlacesLocation {
    @SerializedName("business_status")
    private lateinit var businessStatus: String
    lateinit var geometry: Geometry
    private lateinit var icon: String  //gives back an URL to a picture
    private lateinit var id: String
    lateinit var name: String

    @SerializedName("opening_hours")
    private lateinit var openingHours: OpeningHours
    lateinit var photos: Array<Photo>

    @SerializedName("place_id")
    private lateinit var placeId: String

    @SerializedName("plus_code")
    private lateinit var plusCode: PlusCode

    //maybe use something like <10 * rating * totalRatings> as a metric for measuring relevance of a place
    var rating: Float = 0f
    private lateinit var reference: String
    private lateinit var scope: String
    private lateinit var types: Array<String>

    @SerializedName("user_ratings_total")
    var userRatingsTotal: Int = 0

    /** the field 'vicinity' isn't really usable without changing a lot,
     * since the 'textsearch' and 'nearbysearch' from google don't use the same name for it*/
    private lateinit var vicinity: String


    //other members not used by the API
    var score: Float = 0f

    //proximity in 10 meters
    var localProximity: Int = 0

    lateinit var photoURLs: MutableList<String>

    fun hasPhotos(): Boolean {
        return this::photos.isInitialized
    }


    /**
     * Transforms the API-Object into a simplified version, that can be displayed to the screen or saved to the database
     *
     */
    fun transform(): Location {
        if (hasPhotos()) {
            return Location(
                name,
                "",
                geometry.location,
                rating,
                photoURLs[0],
                Type.GOOGLE,
                ""
            )
        } else {
            return Location(
                name,
                "",
                geometry.location,
                rating,
                "",
                Type.GOOGLE,
                ""
            )
        }
    }
}
