package com.example.beta.domain

import com.example.beta.data.BreedRepository
import com.example.beta.domain.model.Breed
import javax.inject.Inject

class GetBreeds @Inject constructor(private val repository: BreedRepository) {
    suspend operator fun invoke(): List<Breed> {
        // Try to get breeds from the API
        val breedsFromApi = repository.getAllBreedsFromApi()

        // If breeds are fetched from API, clear the database and insert the new breeds
        if (breedsFromApi.isNotEmpty()) {
            repository.clearBreeds()
            repository.insertBreeds(breedsFromApi)
            return breedsFromApi
        }

        // If the API call did not return any breeds, fall back to the database
        return repository.getAllBreedsFromDatabase()
    }
}
