package com.example.beta.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.SubBreedEntity

@Database(entities = [BreedEntity::class, SubBreedEntity::class], version = 1, exportSchema = true)
abstract class BreedDatabase: RoomDatabase() {

    abstract fun getBreedDao():BreedDao

}