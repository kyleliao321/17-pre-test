package com.a17_pre_test.data.retrofit.services

import com.a17_pre_test.data.retrofit.responses.GetUsersByNameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubUserService {
    @GET("/search/users")
    suspend fun getUsersByName(
        @Query("q") matchKeyWord: String,
        @Query("per_page") pageSize: Int,
        @Query("page") pageOffset: Int
    ): Response<GetUsersByNameResponse>
}