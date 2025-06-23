package com.arch.example.datastore.test

import androidx.datastore.core.DataStore
import com.arch.example.datastore.di.DataStoreModule
import com.arch.example.datastore.serializers.AuthPrefsDataSerializer
import com.arch.example.datastore.serializers.PreferencesSerializer
import com.arch.example.entities.Preferences
import com.arch.example.entities.auth.AuthPrefsData
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
internal object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesPreferencesDataStore(
        serializer: PreferencesSerializer
    ): DataStore<Preferences> = InMemoryDataStore(serializer.defaultValue)

    @Provides
    @Singleton
    fun providesAuthDataStore(
        serializer: AuthPrefsDataSerializer,
    ): DataStore<AuthPrefsData> = InMemoryDataStore(serializer.defaultValue)
}
