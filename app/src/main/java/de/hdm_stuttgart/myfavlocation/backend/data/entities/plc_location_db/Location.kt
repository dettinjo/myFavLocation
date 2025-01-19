package de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import de.hdm_stuttgart.myfavlocation.backend.data.enumerations.Type

/**
 * the uniform model for own locations and locations provided by the google-places-api
 * should be used (instead of 'PlacesLocation') anywhere in the app and can be stored in the local db
 * @property name
 * @property description
 * @property position
 * @property rating
 * @property photoUrls depending on if the location stems from google-places-api or is user-created, this contains either a URL or a filepath
 * @property type used to differentiate between own locations and google-places-locations
 * @property category
 * @property identity primary key
 * @property localProximity how far the location is away from the user (not saved in db, only updated when fetched from db)
 */
@Entity
data class Location(
    var name: String?,
    var description: String?,
    @Embedded(prefix = "position_")
    val position: Position?,
    var rating: Float,
    val photoUrls: String?,
    val type: Type?,
    var category: String?
) : Comparable<Location>, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var identity: Int = 0
    @Ignore
    var localProximity: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Position::class.java.classLoader),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readParcelable(Type::class.java.classLoader),
        parcel.readString()
    ) {
        identity = parcel.readInt()
        localProximity = parcel.readInt()
    }

    override fun compareTo(other: Location): Int {
        return if (other.localProximity > localProximity) {
            -1
        } else {
            1
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(position, flags)
        parcel.writeFloat(rating)
        parcel.writeString(photoUrls)
        parcel.writeParcelable(type, flags)
        parcel.writeString(category)
        parcel.writeInt(identity)
        parcel.writeInt(localProximity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}