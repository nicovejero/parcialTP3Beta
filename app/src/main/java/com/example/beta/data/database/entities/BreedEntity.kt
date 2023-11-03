package com.example.beta.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.domain.model.Breed

@Entity(tableName = "breed_table")
data class BreedEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "breed") val breed: String,
)


fun Breed.toDatabase() = BreedEntity(breed = breed)