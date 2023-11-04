package com.example.beta.data.model

import com.google.gson.annotations.SerializedName

data class BreedsApiResponse(
    @SerializedName("message") val message: Map<String, List<String>>,
    @SerializedName("status") val status: String
)
