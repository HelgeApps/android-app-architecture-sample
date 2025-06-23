package com.arch.example.datastore

import com.arch.example.datastore.implementation.AuthPrefsDataSourceImpl
import com.arch.example.datastore.test.InMemoryDataStore
import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthDataStoreTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: AuthPrefsDataSource

    @Before
    fun setup() {
        subject = AuthPrefsDataSourceImpl(InMemoryDataStore(AuthPrefsData()))
    }

    @Test
    fun shouldAllDataBeNullByDefault() = testScope.runTest {
        val data = subject.authPrefsData().first()
        assertNull(data.tokenType)
        assertNull(data.accessToken)
        assertNull(data.refreshToken)
    }

    @Test
    fun shouldAllDataBeNonNullWhenSet() = testScope.runTest {
        subject.update(
            AuthPrefsData(
                tokenType = "bearer",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
            )
        )
        val data = subject.authPrefsData().first()
        assertNotNull(data.tokenType)
        assertNotNull(data.accessToken)
        assertNotNull(data.refreshToken)
    }
}
