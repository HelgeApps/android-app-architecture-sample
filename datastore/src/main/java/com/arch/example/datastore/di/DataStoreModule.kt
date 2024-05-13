package com.arch.example.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.arch.example.common.AppDispatchers
import com.arch.example.common.Dispatcher
import com.arch.example.common.di.ApplicationScope
import com.arch.example.datastore.AuthDataStore
import com.arch.example.datastore.PreferencesDataStore
import com.arch.example.datastore.implementation.AuthDataStoreImpl
import com.arch.example.datastore.implementation.PreferencesDataStoreImpl
import com.arch.example.datastore.serializers.AuthDataSerializer
import com.arch.example.datastore.serializers.PreferencesSerializer
import com.arch.example.entities.Preferences
import com.arch.example.entities.auth.AuthData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

private const val PREFERENCES_DATASTORE_FILE_NAME = "preferences.pb"
private const val AUTH_DATASTORE_FILE_NAME = "auth.pb"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    internal fun providesPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.Default) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        serializer: PreferencesSerializer
    ): DataStore<Preferences> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler = ReplaceFileCorruptionHandler {
                serializer.defaultValue
            },
        ) {
            context.dataStoreFile(PREFERENCES_DATASTORE_FILE_NAME)
        }

    @Provides
    internal fun providePreferencesManager(
        datastore: DataStore<Preferences>,
    ): PreferencesDataStore = PreferencesDataStoreImpl(datastore)


    @Provides
    @Singleton
    internal fun providesAuthDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.Default) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        serializer: AuthDataSerializer
    ): DataStore<AuthData> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler = ReplaceFileCorruptionHandler {
                serializer.defaultValue
            },
        ) {
            context.dataStoreFile(AUTH_DATASTORE_FILE_NAME)
        }

    @Provides
    internal fun provideAuthDataManager(
        datastore: DataStore<AuthData>,
    ): AuthDataStore = AuthDataStoreImpl(datastore)
}