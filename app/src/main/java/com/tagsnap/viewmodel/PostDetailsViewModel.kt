package com.tagsnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.tagsnap.data.models.Comment
import com.tagsnap.data.repositories.CommentRepository
import kotlinx.coroutines.flow.Flow

class PostDetailsViewModel(
    private val commentRepository: CommentRepository = CommentRepository()
) : ViewModel() {
    fun topComments(postId: String): Flow<PagingData<Comment>> =
        commentRepository.getComments(postId, "qualityScore").flow

    fun newComments(postId: String): Flow<PagingData<Comment>> =
        commentRepository.getComments(postId, "createdAt").flow

    fun risingComments(postId: String): Flow<PagingData<Comment>> =
        commentRepository.getComments(postId, "risingScore").flow

    fun controversialComments(postId: String): Flow<PagingData<Comment>> =
        commentRepository.getComments(postId, "controversialScore").flow
}
