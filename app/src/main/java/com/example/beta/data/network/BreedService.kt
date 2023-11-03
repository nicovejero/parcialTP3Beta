package com.example.beta.data.network

import com.example.beta.data.model.BreedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedService @Inject constructor(private val service:BreedApiClient) {

    suspend fun getBreeds(): List<BreedModel> {
        return withContext(Dispatchers.IO) {
            val response = service.getAllBreeds()
            response.body() ?: emptyList()
        }
    }

}