package com.example.beta.data.network

import com.example.beta.data.model.BreedModel
import com.example.beta.data.model.BreedsApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubBreedService @Inject constructor(private val service: BreedApiClient) {

    suspend fun getBreeds(): List<BreedModel> {
        return withContext(Dispatchers.IO) {
            val response = service.getAllBreeds()
            val breedsApiResponse = response.body()
            if (breedsApiResponse != null && breedsApiResponse.status == "success") {
                convertApiResponseToBreedModels(breedsApiResponse)
            } else {
                emptyList()
            }
        }
    }

    private fun convertApiResponseToBreedModels(apiResponse: BreedsApiResponse): List<BreedModel> {
        // Assuming BreedModel represents a single breed and its sub-breeds as a list
        return apiResponse.message.map { (breed, subBreeds) ->
            BreedModel(breedName = breed, subBreeds = subBreeds)
        }
    }
}