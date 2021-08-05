package com.team3205.junior.data.di

import android.app.Application
import androidx.room.Room
import com.team3205.junior.data.db.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DI слоя базы данных
 */
@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    /**
     * Provide базы данных
     */
    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDb =
        Room.databaseBuilder(application,AppDb::class.java,"YetAnotherGithub.db")
            .build()
}