package com.example.beta.data.model

import android.os.Parcel
import android.os.Parcelable

data class PetModel(
    var petId: String = "",
    val petName: String = "",
    val petBreed: String = "",
    val petSubBreed: String = "",
    val urlImage: List<String> = emptyList(),
    val petAge: Int = 0,
    val petWeight: Double = 0.0,
    val petGender: Boolean = false,
    var petOwner: String = "",
    var petLocation: String = "",
    val petAdopted: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<String>().apply { parcel.readStringList(this) },
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    constructor() : this("", "", "", "", emptyList(), 0, 0.0, false, "", "")

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
            "petWeight" to petWeight,
            "petGender" to petGender,
            "petOwner" to petOwner,
            "petLocation" to petLocation,
            "petAdopted" to petAdopted
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(petId)
        writeString(petName)
        writeString(petBreed)
        writeString(petSubBreed)
        writeStringList(urlImage)
        writeInt(petAge)
        writeDouble(petWeight)
        writeBoolean(petGender)
        writeString(petOwner)
        writeString(petLocation)
        writeBoolean(petAdopted)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PetModel> = object : Parcelable.Creator<PetModel> {
            override fun createFromParcel(parcel: Parcel): PetModel {
                return PetModel(parcel)
            }

            override fun newArray(size: Int): Array<PetModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
