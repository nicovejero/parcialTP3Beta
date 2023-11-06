package com.example.beta.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.beta.domain.model.Breed

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "breed") val breedName: String
)

@Entity(tableName = "subbreeds")
data class SubBreedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "breed_id") val breedId: Int, // Foreign key referencing the parent breed
    @ColumnInfo(name = "subbreed_name") val subBreedName: String
)

data class BreedWithSubBreeds(
    @Embedded val breed: BreedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "breed_id" // This should match the foreign key column name in SubBreedEntity
    )
    val subBreeds: List<SubBreedEntity>
)

fun BreedWithSubBreeds.toDomain(): Breed {
    return Breed(
        breedName = this.breed.breedName,
        subBreeds = this.subBreeds.map { it.subBreedName }
    )
}
