package com.arch.example.common

import app.cash.turbine.test
import com.arch.example.common.result.AsyncResult
import com.arch.example.common.result.asResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class AsyncResultKtTest {

    @Test
    fun Async_Result_catches_errors() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }
            .asResult()
            .test {
                assertEquals(AsyncResult.Loading, awaitItem())
                assertEquals(AsyncResult.Success(1), awaitItem())

                when (val errorResult = awaitItem()) {
                    is AsyncResult.Error -> assertEquals(
                        "Test Done",
                        errorResult.exception.message,
                    )

                    AsyncResult.Loading,
                    is AsyncResult.Success,
                    -> throw IllegalStateException(
                        "The flow should have emitted an Error Result",
                    )
                }

                awaitComplete()
            }
    }
}
