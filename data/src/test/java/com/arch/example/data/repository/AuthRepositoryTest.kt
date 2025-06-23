package com.arch.example.data.repository

import com.arch.example.data.AuthRepository
import com.arch.example.data.implementation.AuthRepositoryImpl
import com.arch.example.data.testdoubles.TestAuthNetworkDataSource
import com.arch.example.datastore.AuthPrefsDataSource
import com.arch.example.datastore.implementation.AuthPrefsDataSourceImpl
import com.arch.example.datastore.test.InMemoryDataStore
import com.arch.example.entities.auth.AuthPrefsData
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
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private lateinit var subject: AuthRepository

    private lateinit var authPrefsDataSource: AuthPrefsDataSource

    private lateinit var authNetworkDataSource: AuthNetworkDataSource

    @Before
    fun setup() {
        authNetworkDataSource = TestAuthNetworkDataSource(
            ioDispatcher = testDispatcher,
            moshi = Moshi.Builder()
                .add(DateTimeTypeAdapter())
                .add(OauthGrantTypeAdapter())
                .build()
        )

        authPrefsDataSource = AuthPrefsDataSourceImpl(InMemoryDataStore(AuthPrefsData()))

        subject = AuthRepositoryImpl(
            authNetworkDataSource = authNetworkDataSource,
            authPrefsDataSource = authPrefsDataSource,
        )
    }

    @Test
    fun authDataRepository_default_auth_data_is_correct() =
        testScope.runTest {
            assertEquals(
                AuthPrefsData(),
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
                AuthPrefsData(),
                subject.authData().first(),
            )
        }
}
