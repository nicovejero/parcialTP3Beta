package com.example.beta.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subbreeds",
    foreignKeys = [ForeignKey(
        entity = BreedEntity::class,
        parentColumns = ["id"],
        childColumns = ["breed_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["breed_id"])] // Add this line to define an index on the breed_id column
)
data class SubBreedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "breed_id") val breedId: Int, // This assumes you have the breed ID available
    @ColumnInfo(name = "subbreed_names") val subBreedNames: String // This will store all sub-breed names concatenated by a delimiter
)

fun SubBreedEntity.toDatabase() = SubBreedEntity(
    breedId = breedId,
    subBreedNames = subBreedNames
)