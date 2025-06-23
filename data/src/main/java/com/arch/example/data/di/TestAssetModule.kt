package com.arch.example.data.di

import android.content.Context
import com.arch.example.data.test.TestAssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object TestAssetModule {

    @Provides
    @Singleton
    fun providesDemoAssetManager(
        @ApplicationContext context: Context,
    ): TestAssetManager = TestAssetManager(context.assets::open)
}