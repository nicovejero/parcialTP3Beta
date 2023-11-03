package com.example.beta.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beta.data.database.entities.BreedEntity

@Dao
interface BreedDao {

    @Query("SELECT * FROM breed_table ORDER BY author DESC")
    suspend fun getAllBreeds():List<BreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breedss:List<BreedEntity>)

    @Query("DELETE FROM breed_table")
    suspend fun deleteAllBreeds()
}