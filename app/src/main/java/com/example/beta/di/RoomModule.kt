//Dependency Injection
package com.example.beta.di

import android.content.Context
import androidx.room.Room
import com.example.beta.data.database.BreedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val BREED_DATABASE_NAME = "breed_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BreedDatabase::class.java, BREED_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideBreedDao(db: BreedDatabase) = db.getBreedDao()
}