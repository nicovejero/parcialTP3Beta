package com.example.beta.data

import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.data.network.BreedService
import com.example.beta.domain.model.Breed
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

    suspend fun insertBreeds(breeds: List<Breed>) {
        breedDao.deleteAllBreeds()  // Clear the current breeds to prevent duplication

        breeds.forEach { breed ->
            // Insert BreedEntity and get its generated ID
            val breedEntity = BreedEntity(breedName = breed.breedName)
            val breedId = breedDao.insertBreed(breedEntity)  // This should return the generated ID

            // Ensure breedId is valid (non-zero) before proceeding
            if (breedId > 0) {
                // Prepare SubBreedEntities with the generated breedId
                val subBreedEntities = breed.subBreeds.map { subBreedName ->
                    SubBreedEntity(breedId = breedId.toInt(), subBreedName = subBreedName)
                }

                breedDao.insertAllSubBreeds(subBreedEntities)
            } else {
                // Handle the error scenario where breedId was not generated properly
                // You could throw an exception or handle it based on your application needs
            }
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

    suspend fun clearBreeds() {
        breedDao.deleteAllBreeds()
    }
}
