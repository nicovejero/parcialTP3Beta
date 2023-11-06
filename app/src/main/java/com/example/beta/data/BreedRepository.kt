package com.example.beta.data

import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.BreedWithSubBreeds
import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.data.database.entities.toDomain
import com.example.beta.data.model.BreedModel
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
        // Use the correct DAO method to fetch breeds with sub-breeds
        return breedDao.getAllBreedsWithSubBreeds().map { it.toDomain() }
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

    suspend fun getSubBreedsForBreed(breedId: Int): List<SubBreed> {
        return breedDao.getSubBreedsForBreed(breedId).map { it.toDomain() }
    }

    suspend fun clearBreeds() {
        breedDao.deleteAllBreeds()
    }
}
