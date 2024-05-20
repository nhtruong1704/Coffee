package com.nhtruong.coffee.model

import android.os.Parcel
import android.os.Parcelable



data class FavoriteModel(
    val drinkId: String = "",
    val picUrl: String = "",
    val title: String = "",
    val price: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        drinkId = parcel.readString().orEmpty(),
        picUrl = parcel.readString().orEmpty(),
        title=parcel.readString().orEmpty(),
        price=parcel.readString().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(drinkId)
        parcel.writeString(picUrl)
        parcel.writeString(title)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteModel> {
        override fun createFromParcel(parcel: Parcel): FavoriteModel {
            return FavoriteModel(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteModel?> {
            return arrayOfNulls(size)
        }
    }
}



