package com.example.beta.data.model

import com.google.gson.annotations.SerializedName

data class BreedModel(
    @SerializedName("breed") val breed: String,
    @SerializedName("author") val author: String,
    @SerializedName("category") val category: String
)