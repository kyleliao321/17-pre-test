package com.a17_pre_test.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.a17_pre_test.databinding.FragmentSearchGithubUserCardBinding
import com.a17_pre_test.domain.models.GithubUserDomainModel

class GithubUserAdapter :
    PagingDataAdapter<GithubUserDomainModel, GithubUserAdapter.GithubUserViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val data = getItem(position)

        data?.let {
            holder.bind(it.avatarSource, it.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder =
        GithubUserViewHolder.from(parent)

    class GithubUserViewHolder private constructor(
        private val binding: FragmentSearchGithubUserCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatarUri: Uri, name: String) {
            binding.githubUserAvatar.load(avatarUri) { transformations(CircleCropTransformation()) }
            binding.githubUserName.text = name
        }

        companion object {
            fun from(parent: ViewGroup): GithubUserViewHolder {
                val binding = FragmentSearchGithubUserCardBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )

                return GithubUserViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<GithubUserDomainModel>() {
        override fun areContentsTheSame(
            oldItem: GithubUserDomainModel,
            newItem: GithubUserDomainModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: GithubUserDomainModel,
            newItem: GithubUserDomainModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}