package com.example.beta.data.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val adopted: List<String> = emptyList(),
    val bookmarks: List<String> = emptyList(),
    val urlImage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<String>().apply { parcel.readStringList(this) },
        mutableListOf<String>().apply { parcel.readStringList(this) },
        parcel.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(userId)
        writeString(firstName)
        writeString(lastName)
        writeString(email)
        writeStringList(adopted)
        writeStringList(bookmarks)
        writeString(urlImage)
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
