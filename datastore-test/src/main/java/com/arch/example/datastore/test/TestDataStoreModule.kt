package com.arch.example.datastore.test

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.arch.example.common.di.ApplicationScope
import com.arch.example.datastore.di.DataStoreModule
import com.arch.example.datastore.serializers.AuthDataSerializer
import com.arch.example.datastore.serializers.PreferencesSerializer
import com.arch.example.entities.Preferences
import com.arch.example.entities.auth.AuthData
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
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
        @ApplicationScope scope: CoroutineScope,
        preferencesSerializer: PreferencesSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<Preferences> =
        tmpFolder.testPreferencesDataStore(
            coroutineScope = scope,
            preferencesSerializer = preferencesSerializer,
        )

    @Provides
    @Singleton
    fun providesAuthDataStore(
        @ApplicationScope scope: CoroutineScope,
        authSerializer: AuthDataSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<AuthData> =
        tmpFolder.testAuthDataStore(
            coroutineScope = scope,
            authSerializer = authSerializer,
        )
}

fun TemporaryFolder.testPreferencesDataStore(
    coroutineScope: CoroutineScope,
    preferencesSerializer: PreferencesSerializer = PreferencesSerializer(),
) = DataStoreFactory.create(
    serializer = preferencesSerializer,
    scope = coroutineScope,
) {
    newFile("preferences_test.pb")
}

fun TemporaryFolder.testAuthDataStore(
    coroutineScope: CoroutineScope,
    authSerializer: AuthDataSerializer = AuthDataSerializer(),
) = DataStoreFactory.create(
    serializer = authSerializer,
    scope = coroutineScope,
) {
    newFile("auth_test.pb")
}

