package com.example.beta.data

import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.data.network.BreedService
import com.example.beta.domain.model.Breed
import com.example.beta.domain.model.SubBreed
import com.example.beta.domain.model.toDomain
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val remote: BreedService,
    private val breedDao: BreedDao
) {
    suspend fun getAllBreedsFromApi(): List<Breed> {
        val response = remote.getBreeds()
        return response.message.map { (breedName, subBreeds) ->
            Breed(breedName = breedName, subBreeds = subBreeds)
        }
    }

    suspend fun getAllBreedsFromDatabase(): List<Breed> {
        // Fetch all Breed entities with their associated SubBreeds
        val breedEntities = breedDao.getAllBreeds()
        val breedWithSubBreeds = breedEntities.map { breedEntity ->
            // For each Breed, find its associated SubBreeds
            val subBreeds = breedDao.getSubBreedsForBreed(breedEntity.id).map { it.subBreedName }
            // Map the BreedEntity and its SubBreeds to the domain model
            Breed(breedName = breedEntity.breedName, subBreeds = subBreeds)
        }
        return breedWithSubBreeds
    }

    suspend fun insertBreeds(breeds: List<Breed>) {
        breedDao.deleteAllBreeds()  // Clear the current breeds to prevent duplication

        breeds.forEach { breed ->
            // Use the new DAO function with integrated error checking
            val breedId = breedDao.insertBreedAndCheck(BreedEntity(breedName = breed.breedName))
            val subBreedEntities = breed.subBreeds.map { subBreedName ->
                SubBreedEntity(breedId = breedId.toInt(), subBreedName = subBreedName)
            }
            breedDao.insertAllSubBreeds(subBreedEntities)
        }
    }

    suspend fun getSubBreedsByBreed(breedName: String): List<SubBreed> {
        // Try to find the breed in the database first
        val breedEntity = breedDao.getBreedByName(breedName)
        return breedEntity?.let { breed ->
            // If found, return its sub-breeds as domain models
            breedDao.getSubBreedsForBreed(breed.id).map { subBreedEntity ->
                subBreedEntity.toDomain() // Convert each SubBreedEntity to a domain model
            }
        } ?: emptyList() // If not found, return an empty list of SubBreed
    }



    suspend fun clearBreeds() {
        breedDao.deleteAllBreeds()
    }
}
