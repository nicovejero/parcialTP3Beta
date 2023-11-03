package com.example.beta.domain.model

import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.model.BreedModel

data class Breed(
    val breedName: String,
    val subBreeds: List<String>?
)

// Converts BreedModel from the data layer to Breed domain model
fun BreedModel.toDomain(): Breed {
    return Breed(
        breedName = this.breed,
        subBreeds = this.subBreeds // assuming subBreeds is a List<String> in BreedModel
    )
}

// Converts BreedEntity from the database layer to Breed domain model
fun BreedEntity.toDomain(): Breed {
    return Breed(
        breedName = this.breedName,
        subBreeds = null // Assuming BreedEntity does not hold sub-breeds information
    )
}