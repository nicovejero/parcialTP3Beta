package com.example.beta.domain.model

import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.model.BreedModel

data class Breed (val breed:String, val author:String, val category:String)

fun BreedModel.toDomain() = Breed(breed, author, category)
fun BreedEntity.toDomain() = Breed(breed, author, category)