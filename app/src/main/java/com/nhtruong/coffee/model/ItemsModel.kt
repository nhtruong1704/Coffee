package com.nhtruong.coffee.model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var drinkId: String = "",
    var title: String = "",
    var description: String = "",
    var picUrl: String = "",
    var price: Double = 0.0,
    var priceWithTopping: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0,
    var category: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        drinkId = parcel.readString().orEmpty(),
        title = parcel.readString().orEmpty(),
        description = parcel.readString().orEmpty(),
        picUrl = parcel.readString().orEmpty(),
        price = parcel.readDouble(),
        priceWithTopping = parcel.readDouble(),
        rating = parcel.readDouble(),
        numberInCart = parcel.readInt(),
        category = parcel.readString().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(drinkId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(picUrl)
        parcel.writeDouble(price)
        parcel.writeDouble(priceWithTopping)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }

}

