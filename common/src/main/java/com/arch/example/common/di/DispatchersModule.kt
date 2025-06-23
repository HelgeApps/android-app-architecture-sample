package com.arch.example.common.di

import com.arch.example.common.AppDispatchers
import com.arch.example.common.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * It's recommended to inject dispatchers (then you can easily replace with test dispatchers)
 * Don't hardcode [kotlinx.coroutines.Dispatchers] when creating new coroutines or calling withContext
 * https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Dispatcher(AppDispatchers.IO)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(AppDispatchers.Default)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
