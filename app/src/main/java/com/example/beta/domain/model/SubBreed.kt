package com.example.beta.domain.model

import com.example.beta.data.database.entities.SubBreedEntity

// This class represents a domain model for a sub-breed
data class SubBreed(
    val breedId: Int,          // The parent breed's ID
    val subBreedName: String   // The name of the sub-breed
)

// Conversion from SubBreedEntity to domain model
fun SubBreedEntity.toDomain(): SubBreed {
    return SubBreed(
        breedId = this.breedId,
        subBreedName = this.subBreedName
    )
}

// Conversion from SubBreed domain model to entity. Assumes that the domain model's breedId is valid and present
fun SubBreed.toEntity(): SubBreedEntity {
    return SubBreedEntity(
        breedId = this.breedId,
        subBreedName = this.subBreedName
    )
}
