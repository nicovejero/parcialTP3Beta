package com.example.beta.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.BreedWithSubBreeds
import com.example.beta.data.database.entities.SubBreedEntity

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBreeds(breeds: List<BreedEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSubBreeds(subBreeds: List<SubBreedEntity>)

    @Transaction
    @Query("SELECT * FROM breeds")
    suspend fun getAllBreedsWithSubBreeds(): List<BreedWithSubBreeds>

    @Query("DELETE FROM breeds")
    suspend fun deleteAllBreeds()

    @Query("SELECT * FROM breeds")
    suspend fun getAllBreeds(): List<BreedEntity>

    @Query("SELECT * FROM breeds WHERE breed = :breedName LIMIT 1")
    suspend fun getBreedByName(breedName: String): BreedEntity?

    @Query("SELECT * FROM subbreeds WHERE id = :breedId")
    suspend fun getSubBreedsForBreed(breedId: Int): List<SubBreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreed(breed: BreedEntity): Long

    // Add a new function that includes the error handling
    suspend fun insertBreedAndCheck(breed: BreedEntity): Long {
        val id = insertBreed(breed)
        if (id > 0) {
            return id
        } else {
            throw Exception("Failed to insert breed: ${breed.breedName}")
        }
    }


}