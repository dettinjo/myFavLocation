package de.hdm_stuttgart.myfavlocation.backend.data.enumerations

import android.os.Parcel
import android.os.Parcelable

enum class Type(private var option: String?) : Parcelable {
    OWN("OWN"),
    GOOGLE("GOOGLE");

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(ordinal)
        out.writeString(option)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Type> {

        override fun createFromParcel(parcel: Parcel): Type {
            val type = values()[parcel.readInt()]
            type.option = parcel.readString()
            return type
        }

        override fun newArray(size: Int): Array<Type?> {
            return arrayOfNulls(size)
        }
    }
}