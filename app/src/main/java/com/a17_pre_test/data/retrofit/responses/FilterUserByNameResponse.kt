package com.a17_pre_test.data.retrofit.responses

import com.a17_pre_test.data.models.GithubUserDataModel

data class FilterUserByNameResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GithubUserDataModel>
)
