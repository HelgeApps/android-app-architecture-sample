package com.arch.example.network.di

import android.content.Context
import com.arch.example.common.network.ErrorGlobalHandlerObserver
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.BuildConfig
import com.arch.example.network.adapters.DateTimeTypeAdapter
import com.arch.example.network.adapters.OauthGrantTypeAdapter
import com.arch.example.network.api.AuthApi
import com.arch.example.network.api.PhotoApi
import com.arch.example.network.api.TopicApi
import com.arch.example.network.converters.TopicsOrderConverter
import com.arch.example.network.interceptors.AddTokenInterceptor
import com.arch.example.network.interceptors.HeaderInterceptor
import com.arch.example.network.interceptors.TokenAuthenticator
import com.arch.example.network.manager.AuthNetworkDataSource
import com.arch.example.network.manager.PhotoNetworkDataSource
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.manager.implementation.AuthNetworkDataSourceImpl
import com.arch.example.network.manager.implementation.PhotoNetworkDataSourceImpl
import com.arch.example.network.manager.implementation.TopicNetworkDataSourceImpl
import com.arch.example.network.utils.NetworkErrorConverterHelper
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NetworkModule {

    @Binds
    abstract fun bindErrorGlobalHandlerObserver(
        networkErrorConverterHelper: NetworkErrorConverterHelper
    ): ErrorGlobalHandlerObserver

    companion object {
        private const val HTTP_CLIENT_READ_TIMEOUT = 30L
        private const val HTTP_CLIENT_WRITE_TIMEOUT = 30L
        private const val HTTP_CLIENT_CONNECT_TIMEOUT = 30L

        @Provides
        @Singleton
        fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG || BuildConfig.FLAVOR == "dev") {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

        @Provides
        @Singleton
        fun providesOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            addTokenInterceptor: AddTokenInterceptor,
            headerInterceptor: HeaderInterceptor,
            tokenAuthenticator: TokenAuthenticator
        ): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(HTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(addTokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(
            moshi: Moshi,
            client: OkHttpClient,
            queryConverterFactory: Converter.Factory
        ): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(queryConverterFactory)
            .client(client)
            .build()

        @Provides
        @Singleton
        fun provideQueryConverterFactory(
            topicsOrderConverter: TopicsOrderConverter
        ): Converter.Factory =
            object : Converter.Factory() {
                override fun stringConverter(
                    type: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): Converter<*, String>? {
                    return when (type) {
                        TopicsOrder::class.java -> topicsOrderConverter
                        else -> super.stringConverter(type, annotations, retrofit)
                    }
                }
            }

        @Provides
        @Singleton
        fun providesMoshi() = Moshi.Builder()
            .add(DateTimeTypeAdapter())
            .add(OauthGrantTypeAdapter())
            .build()

        @Provides
        @Singleton
        fun provideNetworkErrorConverterHelper(
            @ApplicationContext context: Context,
            moshi: Moshi
        ): NetworkErrorConverterHelper {
            return NetworkErrorConverterHelper(context, moshi)
        }

        @Provides
        fun provideTopicApi(retrofit: Retrofit): TopicApi = retrofit.create(TopicApi::class.java)

        @Provides
        fun providePhotoApi(retrofit: Retrofit): PhotoApi = retrofit.create(PhotoApi::class.java)

        @Provides
        fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

        @Provides
        fun provideTopicDataSource(
            topicApi: TopicApi,
            networkErrorConverterHelper: NetworkErrorConverterHelper
        ): TopicNetworkDataSource =
            TopicNetworkDataSourceImpl(topicApi, networkErrorConverterHelper)

        @Provides
        fun providePhotoDataSource(
            photoApi: PhotoApi,
            networkErrorConverterHelper: NetworkErrorConverterHelper
        ): PhotoNetworkDataSource =
            PhotoNetworkDataSourceImpl(photoApi, networkErrorConverterHelper)

        @Provides
        fun provideAuthDataSource(
            authApi: AuthApi,
            networkErrorConverterHelper: NetworkErrorConverterHelper
        ): AuthNetworkDataSource = AuthNetworkDataSourceImpl(authApi, networkErrorConverterHelper)
    }
}