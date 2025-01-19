package de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db

import de.hdm_stuttgart.myfavlocation.backend.data.enumerations.Type

class TypeConverter {
    @androidx.room.TypeConverter
    fun fromType(value: Type): Int{
        return value.ordinal
    }

    @androidx.room.TypeConverter
    fun toType(value: Int): Type{
        return when(value){
            0 -> Type.OWN
            1 -> Type.GOOGLE
            else -> Type.OWN
        }
    }


}