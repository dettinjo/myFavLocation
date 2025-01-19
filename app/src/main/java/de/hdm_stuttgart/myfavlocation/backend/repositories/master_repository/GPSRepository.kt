package de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository

import android.content.Context
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position
import de.hdm_stuttgart.myfavlocation.backend.data.R
import de.hdm_stuttgart.myfavlocation.backend.gps.GPSTracker
import kotlin.math.*


class GPSRepository(private var context: Context) {

    /**
     * @return if successful, the current position, else null
     * try to get the current position
     */
    fun getCurrentPosition(): Position? {
        val currentPosition = GPSTracker(context).getLocation()
        return if(currentPosition != null ){
                val lat: Double = currentPosition.latitude
                val lon: Double = currentPosition.longitude
                Position(lat, lon)
            }
            else {
            null
        }
    }


    /**
     * calculate the distance between a single position and a list of other positions
     */
    fun calculateProximity(position: Position, list: List<Location>) {
            list.forEach { e ->
                e.localProximity = calculateProximity(position, e.position!!)
            }
    }

    /**
     * if the GPS has a location, then calculate the distance of a list of other positions to it
     */
    fun calculateProximity(list: List<Location>) {
        val position = getCurrentPosition()
        if (position != null) {
           calculateProximity(position, list)
        }
    }



    companion object {
        @Volatile private var instance : GPSRepository? = null
        fun getInstance(context: Context) =
            instance
                ?: synchronized(this){
                    instance
                        ?: GPSRepository(context)
                            .also { instance = it }
                }

        /**
         * @param currentPosition
         * @param otherPosition
         * @return the distance between the two positions rounded to meters
         *
        This uses the ‘haversine’ formula to calculate the great-circle distance between two points –
        that is, the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’ distance
        between the points (ignoring any hills they fly over).

        Haversine
        formula:
        a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        c = 2 ⋅ atan2( √a, √(1−a) )
        d = R ⋅ c
        where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
        note that angles need to be in radians to pass to trig functions!
        JavaScript:
        const R = 6371e3; // metres
        const φ1 = lat1 * Math.PI/180; // φ, λ in radians
        const φ2 = lat2 * Math.PI/180;
        const Δφ = (lat2-lat1) * Math.PI/180;
        const Δλ = (lon2-lon1) * Math.PI/180;

        const a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
        Math.cos(φ1) * Math.cos(φ2) *
        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        const d = R * c; // in meters
         */
        fun calculateProximity(currentPosition: Position, otherPosition: Position): Int{
            val lat1 = Math.toRadians(currentPosition.lat)
            val lat2 = Math.toRadians(otherPosition.lat)
            val delta1 = Math.toRadians(otherPosition.lat-currentPosition.lat)
            val delta2 = Math.toRadians(otherPosition.lng-currentPosition.lng)
            val a = sin(delta1/2) * sin(delta1/2) +
                    cos(lat1) * cos(lat2) *
                    sin(delta2/2) * sin(delta2/2)
            val c = 2 * atan2(sqrt(a), sqrt(1-a))
            val d = R * c
            return d.roundToInt()
        }
    }
}