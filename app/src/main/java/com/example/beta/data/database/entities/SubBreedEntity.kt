package com.example.beta.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.beta.domain.model.SubBreed


@Entity(
    tableName = "subbreeds",
    foreignKeys = [
        ForeignKey(
            entity = BreedEntity::class,
            parentColumns = ["id"],
            childColumns = ["breedId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["breedId"])]
)
data class SubBreedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val breedId: Int,
    @ColumnInfo(name = "subBreedName") val subBreedName: String
)

fun SubBreedEntity.toDomain(): SubBreed {
    return SubBreed(
        breedId = this.breedId,
        subBreedName = this.subBreedName
    )
}