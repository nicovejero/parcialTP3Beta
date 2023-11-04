package com.example.beta.domain.model

import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.model.BreedsApiResponse

data class Breed(
    val breedName: String,
    val subBreeds: List<String>
)

fun BreedsApiResponse.toDomainModelList(): List<Breed> {
    return this.message.map { (breedName, subBreeds) ->
        Breed(
            breedName = breedName,
            subBreeds = subBreeds
        )
    }
}

// Converts BreedEntity from the database layer to Breed domain model
fun BreedEntity.toDomain(subBreedsList: List<String>): Breed {
    return Breed(
        breedName = this.breedName,
        subBreeds = subBreedsList
    )
}

// Extension function to serialize a list of sub-breeds into JSON for storage
fun Breed.toEntity(): BreedEntity {
    return BreedEntity(
        breedName = this.breedName,
    )
}

fun Breed.toDatabase(): BreedEntity {
    // Assuming you have a constructor in BreedEntity that takes the necessary parameters
    return BreedEntity(
        breedName = this.breedName,
    )
}
