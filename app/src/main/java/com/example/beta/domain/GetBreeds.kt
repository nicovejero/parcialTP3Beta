package com.example.beta.domain

import com.example.beta.data.BreedRepository
import com.example.beta.domain.model.Breed
import com.example.beta.data.database.entities.toDatabase
import javax.inject.Inject

class GetBreeds @Inject constructor(private val repository: BreedRepository) {
    suspend operator fun invoke():List<Breed>{
        val breeds = repository.getAllBreedsFromApi()

        return if(breeds.isNotEmpty()){
            //Elimino los registros para hacer una nueva inserción
            //repository.clearQuotes()

            //Inserto los nuevos registros
            repository.insertBreeds(breeds.map { it.toDatabase() })
            breeds
        }else{
            repository.getAllBreedsFromDatabase()
        }
    }
}