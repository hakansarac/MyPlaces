package com.hakansarac.myplaces.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class PlaceModel(
        val id : Int,
        val title : String?,
        val image : String?,
        val description : String?,
        val date : String?,
        val location : String?,
        val latitude : Double,
        val longitude : Double
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceModel> {
        override fun createFromParcel(parcel: Parcel): PlaceModel {
            return PlaceModel(parcel)
        }

        override fun newArray(size: Int): Array<PlaceModel?> {
            return arrayOfNulls(size)
        }
    }
}
//implement Parcelable and add other methods automatically, do not forget that Strings must be nullable


/**
:Serializable   -> to use putExtra method. otherwise type of class will be unknown type for putExtra method
                   serializable will bring a type into a format which we can pass from one activity to another.
 But Parcelable is significantly faster than Serializable
        **/