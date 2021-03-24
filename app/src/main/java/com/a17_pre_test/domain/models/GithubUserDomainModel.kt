package com.a17_pre_test.domain.models

import android.net.Uri
import com.a17_pre_test.data.models.GithubUserDataModel

data class GithubUserDomainModel(
    val id: Int,
    val name: String,
    val avatarSource: Uri
) {
    companion object {
        fun from(dataModel: GithubUserDataModel): GithubUserDomainModel {
            return GithubUserDomainModel(
                id = dataModel.id,
                name = dataModel.login,
                avatarSource = Uri.parse(dataModel.avatar_url)
            )
        }
    }
}