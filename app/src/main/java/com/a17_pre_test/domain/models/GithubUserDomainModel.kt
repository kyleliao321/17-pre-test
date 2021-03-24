package com.a17_pre_test.domain.models

import com.a17_pre_test.data.models.GithubUserDataModel

data class GithubUserDomainModel(
    val name: String,
    val avatarSource: String
) {
    companion object {
        fun from(dataModel: GithubUserDataModel): GithubUserDomainModel {
            return GithubUserDomainModel(
                name = dataModel.login,
                avatarSource = dataModel.avatar_url
            )
        }
    }
}