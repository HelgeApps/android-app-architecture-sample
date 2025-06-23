package com.arch.example.data.di

import com.arch.example.data.AuthRepository
import com.arch.example.data.PhotoRepository
import com.arch.example.data.PreferencesRepository
import com.arch.example.data.TopicRepository
import com.arch.example.data.implementation.AuthRepositoryImpl
import com.arch.example.data.implementation.PhotoRepositoryImpl
import com.arch.example.data.implementation.PreferencesRepositoryImpl
import com.arch.example.data.implementation.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindTopicRepositoryImpl(impl: TopicRepositoryImpl): TopicRepository

    @Binds
    fun bindPhotoRepositoryImpl(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    fun bindAuthRepositoryImpl(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindPreferencesRepositoryImpl(impl: PreferencesRepositoryImpl): PreferencesRepository
}