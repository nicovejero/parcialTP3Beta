package com.example.beta.domain

import com.example.beta.data.BreedRepository
import com.example.beta.domain.model.Breed
import javax.inject.Inject

class GetBreeds @Inject constructor(private val repository: BreedRepository) {
    suspend operator fun invoke(): List<Breed> {
        return try {
            // Attempt to get breeds from the API
            val breedsFromApi = repository.getAllBreedsFromApi()

            // If successful, clear the database and insert new breeds
            repository.clearBreeds()
            repository.insertBreeds(breedsFromApi)

            // Return the breeds fetched from the API
            breedsFromApi
        } catch (exception: Exception) {
            // If fetching from the API fails, fall back to the database
            repository.getAllBreedsFromDatabase()
        }
    }
}
