package com.example.beta.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.BreedWithSubBreeds

@Dao
interface BreedDao {
    @Transaction
    @Query("SELECT * FROM breeds")
    suspend fun getAllBreedsWithSubBreeds(): List<BreedWithSubBreeds>

    // Function to get all breeds without sub-breeds, if needed
    @Query("SELECT * FROM breeds")
    suspend fun getAllBreeds(): List<BreedEntity>

    // Function to insert all breeds, assuming 'onConflict = REPLACE' as an example strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    // Function to delete all breeds
    @Query("DELETE FROM breeds")
    suspend fun deleteAllBreeds()
}
