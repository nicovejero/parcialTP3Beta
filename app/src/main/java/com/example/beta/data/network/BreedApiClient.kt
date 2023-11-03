package com.example.beta.data.network

import com.example.beta.data.model.BreedModel
import retrofit2.Response
import retrofit2.http.GET

interface BreedApiClient {
    @GET("/api/breeds/list/all")
    suspend fun getAllBreeds(): Response<List<BreedModel>>

    @GET("/api/breeds/list/all")
    suspend fun getBreed(): Response<BreedModel>
}