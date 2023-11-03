package com.example.beta.data.database.entities

import android.os.Parcel
import android.os.Parcelable

data class Pet(
    val petId: String = "",
    val petName: String = "",
    val petBreed: String = "",
    val petSubBreed: String = "",
    val urlImage: String = "",
    val petAge: Int = 0,
    val petGender: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readBoolean()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(petId)
        writeString(petName)
        writeString(petBreed)
        writeString(petSubBreed)
        writeString(urlImage)
        writeInt(petAge)
        writeBoolean(petGender)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Pet> = object : Parcelable.Creator<Pet> {
            override fun createFromParcel(source: Parcel): Pet = Pet(source)
            override fun newArray(size: Int): Array<Pet?> = arrayOfNulls(size)
        }
    }
}
