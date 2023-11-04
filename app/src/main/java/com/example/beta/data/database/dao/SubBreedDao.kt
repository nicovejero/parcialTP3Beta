package com.example.beta.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.beta.data.database.entities.SubBreedEntity

@Dao
interface SubBreedDao {
    // Query to select all sub-breeds of a particular main breed
    @Query("SELECT * FROM subbreeds WHERE breedId = :breedId")
    suspend fun getSubBreedsForBreed(breedId: Int): List<SubBreedEntity>
}
