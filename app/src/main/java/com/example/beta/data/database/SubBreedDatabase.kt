package com.example.beta.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beta.data.database.dao.SubBreedDao
import com.example.beta.data.database.entities.SubBreedEntity
import com.example.beta.data.database.entities.BreedEntity

@Database(entities = [BreedEntity::class, SubBreedEntity::class], version = 1)
abstract class SubBreedDatabase: RoomDatabase() {

    abstract fun getSubBreedDao():SubBreedDao

}