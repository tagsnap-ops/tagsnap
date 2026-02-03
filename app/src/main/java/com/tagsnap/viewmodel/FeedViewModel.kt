package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.tagsnap.data.models.Post
import com.tagsnap.data.repositories.PostRepository
import kotlinx.coroutines.flow.Flow

class FeedViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {
    fun topFeed(): Flow<PagingData<Post>> = postRepository.getPosts("qualityScore").flow
    fun newFeed(): Flow<PagingData<Post>> = postRepository.getPosts("createdAt").flow
    fun risingFeed(): Flow<PagingData<Post>> = postRepository.getPosts("risingScore").flow
    fun controversialFeed(): Flow<PagingData<Post>> = postRepository.getPosts("controversialScore").flow
}
