package com.example.beta.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BreedWithSubBreeds(
    @Embedded val breed: BreedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "breedId"
    )
    val subBreeds: List<SubBreedEntity>
)