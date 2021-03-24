package com.a17_pre_test.data.data_source

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.data.retrofit.services.GithubUserService
import com.a17_pre_test.domain.data_source.RemoteDataSource
import com.a17_pre_test.utils.failure.Failure
import com.a17_pre_test.utils.response.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class RemoteDataSourceImp(
    private val githubUserService: GithubUserService
) : RemoteDataSource {

    override suspend fun getUserNameByName(
        matchPattern: String,
        pageSize: Int,
        page: Int
    ): Response<List<GithubUserDataModel>> = try {
        val res = githubUserService.getUsersByName(matchPattern, pageSize, page)
        val status = res.code()
        val data = res.body()?.items

        if (status == HttpURLConnection.HTTP_OK && data != null) {
            Response.Success(data)
        } else {
            Response.Failure(Failure.ServerError)
        }
    } catch (e: SocketTimeoutException) {
        Response.Failure(Failure.NetworkTimeout)
    }
}