package com.tagsnap.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.tagsnap.data.firebase.FirebaseProvider
import com.tagsnap.data.models.Comment
import com.tagsnap.utils.FirestorePagingSource
import com.tagsnap.utils.mapComment
import kotlinx.coroutines.tasks.await

class CommentRepository {
    private val firestore = FirebaseProvider.firestore

    fun getComments(postId: String, orderBy: String): Pager<Any, Comment> {
        val query = firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .orderBy(orderBy, Query.Direction.DESCENDING)
        return Pager(PagingConfig(pageSize = 20)) {
            FirestorePagingSource(query = query, mapper = ::mapComment)
        }
    }

    suspend fun createComment(comment: Comment) {
        val doc = firestore.collection("comments").document()
        val payload = hashMapOf(
            "commentId" to doc.id,
            "postId" to comment.postId,
            "authorId" to comment.authorId,
            "authorHandle" to comment.authorHandle,
            "parentCommentId" to comment.parentCommentId,
            "text" to comment.text,
            "createdAt" to Timestamp.now(),
            "upvotesCount" to 0,
            "downvotesCount" to 0,
            "reportsCount" to 0,
            "hidesCount" to 0,
            "engagementSecondsTotal" to 0,
            "qualityScore" to 0.0,
            "risingScore" to 0.0,
            "controversialScore" to 0.0,
            "lastScoreUpdate" to Timestamp.now(),
            "moderationFlags" to mapOf("nsfw" to false, "spam" to false, "abusive" to false)
        )
        doc.set(payload).await()
    }

    suspend fun submitVote(commentId: String, voteValue: Int) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val voteId = "${commentId}_$userId"
        val payload = hashMapOf(
            "voteId" to voteId,
            "targetType" to "COMMENT",
            "targetId" to commentId,
            "voterId" to userId,
            "voteValue" to voteValue,
            "voteWeight" to 1.0,
            "createdAt" to Timestamp.now(),
            "deviceHash" to null,
            "isSuspicious" to false
        )
        firestore.collection("votes").document(voteId).set(payload).await()
    }

    suspend fun reportComment(commentId: String, reason: String) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val reportId = "${commentId}_$userId"
        firestore.collection("reports").document(reportId)
            .set(mapOf("reportId" to reportId, "reporterId" to userId, "targetType" to "COMMENT", "targetId" to commentId, "reason" to reason, "createdAt" to Timestamp.now()))
            .await()
    }
}
