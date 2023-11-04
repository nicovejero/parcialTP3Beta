package com.example.beta.data.network

import com.example.beta.data.model.BreedsApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface BreedApiClient {
    @GET("/api/breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedsApiResponse>

    @GET("/api/breeds/list/all")
    suspend fun getBreed(): Response<BreedsApiResponse>
}