package com.example.beta.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subbreeds")
data class SubBreed(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "main_breed_name") val mainBreedName: String,
    @ColumnInfo(name = "sub_breed_name") val subBreedName: String
)
