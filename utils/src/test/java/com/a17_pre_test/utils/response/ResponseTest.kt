package com.a17_pre_test.utils.response

import com.a17_pre_test.test_utils.CoroutineTestRule
import com.a17_pre_test.test_utils.runBlockingTest
import com.a17_pre_test.utils.failure.Failure
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ResponseTest {

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @Test
    fun `should continue response chain when all blocks return success`() {
        // given
        val useCase1: Response<String> = Response.Success("string")
        val useCase2: Response<Int> = Response.Success(1)
        val useCase3: Response<Boolean> = Response.Success(false)

        // when
        var result: Response<Boolean>? = null

        mainCoroutineRule.runBlockingTest {
            result = useCase1
                .then {
                    useCase2
                }
                .then {
                    useCase3
                }
                .catch {}
        }

        // then
        result shouldBeInstanceOf Response.Success::class
        (result as Response.Success).data shouldBeEqualTo false
    }

    @Test
    fun `should catch failure when first response is failed`() {
        // given
        val useCase1: Response<String> = Response.Failure(Failure.NetworkTimeout)
        val useCase2: Response<Int> = Response.Success(1)
        val useCase3: Response<Boolean> = Response.Success(false)

        // when
        var result: Response<Boolean>? = null

        mainCoroutineRule.runBlockingTest {
            result = useCase1
                .then {
                    // this should not be called
                    true shouldBeEqualTo false
                    useCase2
                }
                .then {
                    // this should not be called
                    true shouldBeEqualTo false
                    useCase3
                }
                .catch {
                    it shouldBeInstanceOf Failure.NetworkTimeout::class
                }
        }

        // then
        result shouldBeInstanceOf Response.Failure::class
        (result as Response.Failure).failure shouldBeEqualTo Failure.NetworkTimeout
    }
}