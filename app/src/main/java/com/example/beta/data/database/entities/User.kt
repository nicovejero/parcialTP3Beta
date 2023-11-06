package com.example.beta.data.database.entities

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val adopted: List<Pet> = emptyList(),
    val bookmarks: List<Pet> = emptyList(),
    val urlImage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        userId = parcel.readString() ?: "",
        firstName = parcel.readString() ?: "",
        lastName = parcel.readString() ?: "",
        email = parcel.readString() ?: "",
        adopted = parcel.createTypedArrayList(Pet.CREATOR) ?: emptyList(),
        bookmarks = parcel.createTypedArrayList(Pet.CREATOR) ?: emptyList(),
        urlImage = parcel.readString() ?: ""
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
