package com.a17_pre_test.data.data_source

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.data.retrofit.responses.GetUsersByNameResponse
import com.a17_pre_test.data.retrofit.services.GithubUserService
import com.a17_pre_test.domain.data_source.RemoteDataSource
import com.a17_pre_test.test_utils.CoroutineTestRule
import com.a17_pre_test.test_utils.runBlockingTest
import com.a17_pre_test.utils.failure.Failure
import com.a17_pre_test.utils.response.Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

@RunWith(JUnit4::class)
class RemoteDataSourceImpTest {

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @MockK
    private lateinit var githubUserService: GithubUserService

    private lateinit var dataSource: RemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        dataSource = RemoteDataSourceImp(githubUserService)
    }

    @Test
    fun `verify response when connection fail with timeout`() {
        var result: Response<List<GithubUserDataModel>>? = null

        // given
        val matchPattern = "q"
        val pageSize = 5
        val page = 1
        coEvery {
            githubUserService.getUsersByName(
                any(),
                any(),
                any()
            )
        } throws SocketTimeoutException()

        // when
        mainCoroutineRule.runBlockingTest {
            result = dataSource.getUserNameByName(matchPattern, pageSize, page)
        }

        // then
        result shouldNotBeEqualTo null
        result shouldBeEqualTo Response.Failure(Failure.NetworkTimeout)
    }

    @Test
    fun `verify response when connection fail with unknown error code`() {
        var result: Response<List<GithubUserDataModel>>? = null

        // given
        val matchPattern = "q"
        val pageSize = 5
        val page = 1
        val mockRes = mockk<retrofit2.Response<GetUsersByNameResponse>>(relaxed = true)

        every { mockRes.code() } returns HttpURLConnection.HTTP_UNAUTHORIZED
        every { mockRes.body() } returns null
        coEvery {
            githubUserService.getUsersByName(
                any(),
                any(),
                any()
            )
        } returns mockRes

        // when
        mainCoroutineRule.runBlockingTest {
            result = dataSource.getUserNameByName(matchPattern, pageSize, page)
        }

        // then
        result shouldNotBeEqualTo null
        result shouldBeEqualTo Response.Failure(Failure.ServerError)
    }

    @Test
    fun `verify response when connection succeed with http_ok`() {
        var result: Response<List<GithubUserDataModel>>? = null

        // given
        val matchPattern = "q"
        val pageSize = 5
        val page = 1
        val mockRes = mockk<retrofit2.Response<GetUsersByNameResponse>>(relaxed = true)
        val mockData = mockk<GetUsersByNameResponse>(relaxed = true)
        val mockItems = mockk<List<GithubUserDataModel>>(relaxed = true)

        every { mockData.items } returns mockItems
        every { mockRes.code() } returns HttpURLConnection.HTTP_OK
        every { mockRes.body() } returns mockData
        coEvery {
            githubUserService.getUsersByName(
                any(),
                any(),
                any()
            )
        } returns mockRes

        // when
        mainCoroutineRule.runBlockingTest {
            result = dataSource.getUserNameByName(matchPattern, pageSize, page)
        }

        // then
        result shouldNotBeEqualTo null
        result shouldBeEqualTo Response.Success(mockItems)
    }

}