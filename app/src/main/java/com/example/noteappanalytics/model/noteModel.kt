package com.example.noteappanalytics.model

import android.os.Parcel
import android.os.Parcelable

class noteModel (
    var id: Int? = 0,
    var categoryId: Int? = 0,
    var image: String? = null,
    var name: String? = null,
    var description:String? = null,
    var word_letters: Int? = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(categoryId)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeValue(word_letters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<noteModel> {
        override fun createFromParcel(parcel: Parcel): noteModel {
            return noteModel(parcel)
        }

        override fun newArray(size: Int): Array<noteModel?> {
            return arrayOfNulls(size)
        }
    }
}