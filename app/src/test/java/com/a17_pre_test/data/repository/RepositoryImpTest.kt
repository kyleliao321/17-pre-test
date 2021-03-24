package com.a17_pre_test.data.repository

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.domain.data_source.RemoteDataSource
import com.a17_pre_test.domain.repository.Repository
import com.a17_pre_test.test_utils.CoroutineTestRule
import com.a17_pre_test.test_utils.runBlockingTest
import com.a17_pre_test.utils.failure.Failure
import com.a17_pre_test.utils.response.Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepositoryImpTest {

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @MockK(relaxed = true)
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: Repository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        repository = RepositoryImp(remoteDataSource)
    }

    @Test
    fun `invoke repository's getUserByName should trigger remoteDataSource's getUserByName`() {
        // given
        val matchPattern = "q"
        val pageSize = 5
        val page = 10

        // when
        mainCoroutineRule.runBlockingTest {
            repository.getUserByName(matchPattern, pageSize, page)
        }

        // then
        coVerify(exactly = 1) { remoteDataSource.getUserNameByName(any(), any(), any()) }
    }

    @Test
    fun `repository's getUserByName should return whatever remoteDataSource's getUserByName returns`() {
        var result: Response<List<GithubUserDataModel>>? = null

        // given
        val matchPattern = "q"
        val pageSize = 5
        val page = 10

        coEvery {
            remoteDataSource.getUserNameByName(
                any(),
                any(),
                any()
            )
        } returns Response.Failure(Failure.NetworkTimeout)

        // when
        mainCoroutineRule.runBlockingTest {
            result = repository.getUserByName(matchPattern, pageSize, page)
        }

        // then
        result shouldNotBeEqualTo null
        result shouldBeEqualTo Response.Failure(Failure.NetworkTimeout)
    }
}