package com.example.beta.data

import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.model.BreedModel
import com.example.beta.data.network.BreedService
import com.example.beta.domain.model.Breed
import com.example.beta.domain.model.toDomain
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val remote: BreedService,
    private val breedDao: BreedDao
) {

    suspend fun getAllBreedsFromApi() : List<Breed> {
        val response: List<BreedModel> = remote.getBreeds()
        return response.map { it.toDomain() }
    }

    suspend fun getAllBreedsFromDatabase() : List<Breed>{
        val response: List<BreedEntity> = breedDao.getAllBreeds()
        return response.map { it.toDomain() }
    }

    suspend fun insertBreeds(breeds:List<BreedEntity>) {
        breedDao.insertAll(breeds)
    }

    suspend fun clearBreeds(){
        breedDao.deleteAllBreeds()
    }
}