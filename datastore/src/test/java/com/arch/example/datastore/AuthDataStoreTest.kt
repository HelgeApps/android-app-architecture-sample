package com.arch.example.datastore

import com.arch.example.datastore.implementation.AuthDataStoreImpl
import com.arch.example.datastore.test.testAuthDataStore
import com.arch.example.entities.auth.AuthData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthDataStoreTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: AuthDataStore

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = AuthDataStoreImpl(
            tmpFolder.testAuthDataStore(testScope.backgroundScope),
        )
    }

    @Test
    fun shouldAllDataBeNullByDefault() = testScope.runTest {
        val data = subject.authData().first()
        assertNull(data.tokenType)
        assertNull(data.accessToken)
        assertNull(data.refreshToken)
    }

    @Test
    fun shouldAllDataBeNonNullWhenSet() = testScope.runTest {
        subject.update(
            AuthData(
                tokenType = "bearer",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
            )
        )
        val data = subject.authData().first()
        assertNotNull(data.tokenType)
        assertNotNull(data.accessToken)
        assertNotNull(data.refreshToken)
    }
}
