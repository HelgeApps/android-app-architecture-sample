package com.arch.example.data.repository

import com.arch.example.data.AuthRepository
import com.arch.example.data.implementation.AuthRepositoryImpl
import com.arch.example.data.testdoubles.TestAuthNetworkDataSource
import com.arch.example.datastore.AuthDataStore
import com.arch.example.datastore.implementation.AuthDataStoreImpl
import com.arch.example.datastore.test.testAuthDataStore
import com.arch.example.entities.auth.AuthData
import com.arch.example.network.adapters.DateTimeTypeAdapter
import com.arch.example.network.adapters.OauthGrantTypeAdapter
import com.arch.example.network.manager.AuthNetworkDataSource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private lateinit var subject: AuthRepository

    private lateinit var authDataStore: AuthDataStore

    private lateinit var authNetworkDataSource: AuthNetworkDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        authNetworkDataSource = TestAuthNetworkDataSource(
            ioDispatcher = testDispatcher,
            moshi = Moshi.Builder()
                .add(DateTimeTypeAdapter())
                .add(OauthGrantTypeAdapter())
                .build()
        )

        authDataStore = AuthDataStoreImpl(
            tmpFolder.testAuthDataStore(testScope.backgroundScope),
        )

        subject = AuthRepositoryImpl(
            authNetworkDataSource = authNetworkDataSource,
            authDataStore = authDataStore,
        )
    }

    @Test
    fun authDataRepository_default_auth_data_is_correct() =
        testScope.runTest {
            assertEquals(
                AuthData(),
                subject.authData().first(),
            )
        }

    @Test
    fun authDataRepository_test_login_logout() =
        testScope.runTest {
            subject.login(
                code = "test",
                redirectUri = "unsplash://o_auth"
            ).collect()

            val data = subject.authData().first()
            assertNotNull(data.tokenType)
            assertNotNull(data.accessToken)
            assertNotNull(data.refreshToken)

            subject.logout(withRequest = true)
                .collect()

            assertEquals(
                AuthData(),
                subject.authData().first(),
            )
        }
}
