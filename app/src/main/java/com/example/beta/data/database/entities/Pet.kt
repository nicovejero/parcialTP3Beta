package com.example.beta.data.database.entities

import android.os.Parcel
import android.os.Parcelable

data class Pet(
    var petId: String = "",
    val petName: String = "",
    val petBreed: String = "",
    val petSubBreed: String = "",
    val urlImage: String = "",
    val petAge: Int = 0,
    val petGender: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readBoolean()
    )

    constructor() : this("", "", "", "", "", 0, false)

    override fun describeContents(): Int {
        return 0
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "petId" to petId,
            "petName" to petName,
            "petBreed" to petBreed,
            "petSubBreed" to petSubBreed,
            "urlImage" to urlImage,
            "petAge" to petAge,
            "petGender" to petGender
        )
    }

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
            override fun createFromParcel(parcel: Parcel): Pet {
                return Pet(parcel)
            }

            override fun newArray(size: Int): Array<Pet?> {
                return arrayOfNulls(size)
            }
        }
    }
}
