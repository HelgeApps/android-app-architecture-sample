package com.arch.example.data.testdoubles

import JvmUnitTestAssetManager
import com.arch.example.common.AppDispatchers
import com.arch.example.common.Dispatcher
import com.arch.example.data.test.TestAssetManager
import com.arch.example.network.manager.PhotoNetworkDataSource
import com.arch.example.network.models.photo.NetworkPhoto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

class TestPhotoNetworkDataSource(
    @Dispatcher(AppDispatchers.Default) private val ioDispatcher: CoroutineDispatcher,
    private val moshi: Moshi,
    private val assets: TestAssetManager = JvmUnitTestAssetManager,
) : PhotoNetworkDataSource {

    override suspend fun getPhotos(page: Int, perPage: Int): List<NetworkPhoto> =
        withContext(ioDispatcher) {
            assets.open(PHOTOS_RESPONSE_ASSET).use {
                val photoListType = Types.newParameterizedType(
                    List::class.java,
                    NetworkPhoto::class.java
                )
                moshi.adapter<List<NetworkPhoto>>(photoListType).fromJson(it.source().buffer())!!
            }
        }

    companion object {
        private const val PHOTOS_RESPONSE_ASSET = "photos_response.json"
    }
}
