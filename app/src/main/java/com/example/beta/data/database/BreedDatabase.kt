package com.example.beta.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beta.data.database.dao.BreedDao
import com.example.beta.data.database.dao.BreedTypeConverters
import com.example.beta.data.database.entities.BreedEntity
import com.example.beta.data.database.entities.SubBreedEntity

@Database(entities = [BreedEntity::class, SubBreedEntity::class], version = 3, exportSchema = false)
@TypeConverters(BreedTypeConverters::class)
abstract class BreedDatabase: RoomDatabase() {

    abstract fun getBreedDao():BreedDao

}