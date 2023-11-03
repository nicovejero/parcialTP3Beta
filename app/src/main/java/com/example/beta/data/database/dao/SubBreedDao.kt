package com.example.beta.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beta.data.database.entities.SubBreedEntity

@Dao
interface SubBreedDao {
    // Existing function to get all sub-breeds for a breed
    @Query("SELECT * FROM subbreeds WHERE breed_id = :breedId")
    suspend fun getSubBreedsForBreed(breedId: Int): List<SubBreedEntity>

    // Function to insert a list of sub-breeds into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subBreeds: List<SubBreedEntity>)

    // Function to delete all sub-breeds for a specific breed
    @Query("DELETE FROM subbreeds WHERE breed_id = :breedId")
    suspend fun deleteSubBreedsForBreed(breedId: Int)
}
