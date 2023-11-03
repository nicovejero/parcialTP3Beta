package com.example.beta.domain.model

import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.data.model.SubBreedModel

// This class represents a domain model for a sub-breed
data class SubBreed(
    val breedId: Int,          // Add breedId to link with the parent breed
    val breedName: String,
    val subBreeds: List<String>
)

// Converts SubBreedModel to SubBreed domain model. Assumes SubBreedModel contains a breed and a list of its sub-breeds
fun SubBreedModel.toDomain(breedId: Int): SubBreed {  // Add breedId parameter as it's necessary for the domain model
    return SubBreed(
        breedId = breedId,
        breedName = this.breed, // Assuming 'breed' is the breed name in SubBreedModel
        subBreeds = this.subBreeds ?: emptyList() // Assuming 'subBreeds' is a list of sub-breed names
    )
}

// Conversion from SubBreedEntity to domain model
fun SubBreedEntity.toDomain(): SubBreed {
    val subBreedList = if (this.subBreedNames.isNotEmpty()) {
        this.subBreedNames.split(",") // Splitting the stored concatenated string into a list
    } else {
        emptyList()
    }
    return SubBreed(
        breedId = this.breedId, // Include breedId in the conversion
        breedName = this.subBreedNames,
        subBreeds = subBreedList
    )
}

// Conversion from SubBreed domain model to entity. Assumes that the domain model's breedId is valid and present
fun SubBreed.toEntity(): SubBreedEntity {
    return SubBreedEntity(
        breedId = this.breedId, // Use the breedId from the domain model
        subBreedNames = this.subBreeds.joinToString(",") // Joining the list into a single string for storage
    )
}
