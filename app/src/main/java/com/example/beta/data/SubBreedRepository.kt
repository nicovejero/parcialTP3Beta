package com.example.beta.data

import com.example.beta.data.database.dao.SubBreedDao
import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.domain.model.SubBreed
import com.example.beta.domain.model.toDomain
import javax.inject.Inject

class SubBreedRepository @Inject constructor(
    private val subBreedDao: SubBreedDao
) {
    // This method will fetch all sub-breeds for a specific breed from the database
    suspend fun getSubBreedsForBreed(breedId: Int): List<SubBreed> {
        val subBreedEntities: List<SubBreedEntity> = subBreedDao.getSubBreedsForBreed(breedId)
        // Assuming that each SubBreedEntity corresponds to a single sub-breed and not a list
        // The grouping by breedId is assumed to be handled by the DAO query
        return subBreedEntities.map { it.toDomain() }
    }

    // Insert a list of sub-breeds into the database
    suspend fun insertSubBreeds(subBreeds: List<SubBreedEntity>) {
        subBreedDao.insertAll(subBreeds)
    }

    // Clear all sub-breeds from the database for a specific breed
    suspend fun clearSubBreedsForBreed(breedId: Int) {
        subBreedDao.deleteSubBreedsForBreed(breedId)
    }
}
