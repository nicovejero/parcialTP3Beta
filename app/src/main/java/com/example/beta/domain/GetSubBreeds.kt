package com.example.beta.domain

import com.example.beta.data.BreedRepository
import com.example.beta.domain.model.SubBreed
import javax.inject.Inject

class GetSubBreeds @Inject constructor(private val repository: BreedRepository) {
    suspend operator fun invoke(breedId: Int): List<SubBreed> {
        // Try to get sub-breeds for any given breed name from the repository
        return repository.getSubBreedsForBreed(breedId)
    }
}
