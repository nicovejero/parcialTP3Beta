package com.example.beta.data.model

import com.google.gson.annotations.SerializedName

data class SubBreedModel(
    @SerializedName("breed") val breed: String,
    @SerializedName("subBreeds") val subBreeds: List<String>
)


