package com.example.beta.data.database.entities

import android.os.Parcel
import android.os.Parcelable
import com.example.beta.data.model.PetModel

data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val adopted: List<PetModel> = emptyList(),
    val bookmarks: List<PetModel> = emptyList(),
    val urlImage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(PetModel.CREATOR) ?: emptyList(),
        parcel.createTypedArrayList(PetModel.CREATOR) ?: emptyList(),
        parcel.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeTypedList(adopted)
        parcel.writeTypedList(bookmarks)
        parcel.writeString(urlImage)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
