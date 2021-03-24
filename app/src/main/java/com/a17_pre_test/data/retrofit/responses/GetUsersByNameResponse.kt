package com.a17_pre_test.data.retrofit.responses

import com.a17_pre_test.data.models.GithubUserDataModel

data class GetUsersByNameResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GithubUserDataModel>
)
