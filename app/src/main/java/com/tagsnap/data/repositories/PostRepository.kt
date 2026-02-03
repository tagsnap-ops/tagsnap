package com.tagsnap.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.tagsnap.data.firebase.FirebaseProvider
import com.tagsnap.data.models.Post
import com.tagsnap.data.models.PostType
import com.tagsnap.utils.FirestorePagingSource
import com.tagsnap.utils.mapPost
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val firestore = FirebaseProvider.firestore

    fun getPosts(orderBy: String): Pager<Any, Post> {
        val query = firestore.collection("posts")
            .orderBy(orderBy, Query.Direction.DESCENDING)
        return Pager(PagingConfig(pageSize = 20)) {
            FirestorePagingSource(query = query, mapper = ::mapPost)
        }
    }

    fun getPostsByTopic(topicId: String, orderBy: String): Pager<Any, Post> {
        val query = firestore.collection("posts")
            .whereEqualTo("topicId", topicId)
            .orderBy(orderBy, Query.Direction.DESCENDING)
        return Pager(PagingConfig(pageSize = 20)) {
            FirestorePagingSource(query = query, mapper = ::mapPost)
        }
    }

    suspend fun createPost(post: Post) {
        val doc = firestore.collection("posts").document()
        val payload = hashMapOf(
            "postId" to doc.id,
            "authorId" to post.authorId,
            "authorHandle" to post.authorHandle,
            "type" to post.type.name,
            "text" to post.text,
            "mediaUrls" to post.mediaUrls,
            "pollOptions" to post.pollOptions,
            "pollVotesMap" to post.pollVotesMap,
            "topicId" to post.topicId,
            "createdAt" to Timestamp.now(),
            "upvotesCount" to 0,
            "downvotesCount" to 0,
            "savesCount" to 0,
            "sharesCount" to 0,
            "reportsCount" to 0,
            "hidesCount" to 0,
            "engagementSecondsTotal" to 0,
            "qualityScore" to 0.0,
            "risingScore" to 0.0,
            "controversialScore" to 0.0,
            "lastScoreUpdate" to Timestamp.now(),
            "pinnedCommentId" to null,
            "moderationFlags" to mapOf("nsfw" to false, "spam" to false, "abusive" to false)
        )
        doc.set(payload).await()
    }

    suspend fun updateEngagement(postId: String, seconds: Long) {
        firestore.collection("posts").document(postId)
            .update("engagementSecondsTotal", seconds)
            .await()
    }

    suspend fun submitVote(postId: String, voteValue: Int) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val voteId = "${postId}_$userId"
        val payload = hashMapOf(
            "voteId" to voteId,
            "targetType" to "POST",
            "targetId" to postId,
            "voterId" to userId,
            "voteValue" to voteValue,
            "voteWeight" to 1.0,
            "createdAt" to Timestamp.now(),
            "deviceHash" to null,
            "isSuspicious" to false
        )
        firestore.collection("votes").document(voteId).set(payload).await()
    }

    suspend fun savePost(postId: String) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val saveId = "${postId}_$userId"
        firestore.collection("saves").document(saveId)
            .set(mapOf("saveId" to saveId, "userId" to userId, "postId" to postId, "createdAt" to Timestamp.now()))
            .await()
    }

    suspend fun reportPost(postId: String, reason: String) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val reportId = "${postId}_$userId"
        firestore.collection("reports").document(reportId)
            .set(mapOf("reportId" to reportId, "reporterId" to userId, "targetType" to "POST", "targetId" to postId, "reason" to reason, "createdAt" to Timestamp.now()))
            .await()
    }

    suspend fun hidePost(postId: String) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val hideId = "${postId}_$userId"
        firestore.collection("hides").document(hideId)
            .set(mapOf("hideId" to hideId, "userId" to userId, "postId" to postId, "createdAt" to Timestamp.now()))
            .await()
    }

    suspend fun sharePost(postId: String) {
        val userId = FirebaseProvider.auth.currentUser?.uid ?: return
        val shareId = "${postId}_$userId"
        firestore.collection("shares").document(shareId)
            .set(mapOf("shareId" to shareId, "userId" to userId, "postId" to postId, "createdAt" to Timestamp.now()))
            .await()
    }

    suspend fun uploadMedia(bytes: ByteArray, path: String): String {
        val ref = FirebaseProvider.storage.reference.child(path)
        ref.putBytes(bytes).await()
        return ref.downloadUrl.await().toString()
    }
}
