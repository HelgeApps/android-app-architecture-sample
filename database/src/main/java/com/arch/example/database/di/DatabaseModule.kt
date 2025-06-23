package com.arch.example.database.di

import android.content.Context
import com.arch.example.database.AppDataBase
import com.arch.example.database.dao.TopicDao
import com.arch.example.database.manager.TopicDbManager
import com.arch.example.database.manager.implementation.TopicDbManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    @Provides
    fun provideTopicsDao(dataBase: AppDataBase): TopicDao = dataBase.getTopicsDao()

    @Provides
    fun provideTopicDbManager(topicDao: TopicDao): TopicDbManager = TopicDbManagerImpl(topicDao)
}