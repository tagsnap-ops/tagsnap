package com.tagsnap.data.models

import com.google.firebase.Timestamp

enum class PostType { TEXT, IMAGE, VIDEO, AUDIO, POLL, CAROUSEL }

enum class VoteTarget { POST, COMMENT }

data class AppUser(
    val uid: String = "",
    val handle: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val reputationPoints: Int = 0,
    val badges: List<String> = emptyList(),
    val isShadowBanned: Boolean = false,
    val lastActiveAt: Timestamp = Timestamp.now(),
    val avatarUrl: String = ""
)

data class Post(
    val postId: String = "",
    val authorId: String = "",
    val authorHandle: String = "",
    val type: PostType = PostType.TEXT,
    val text: String = "",
    val mediaUrls: List<String> = emptyList(),
    val pollOptions: List<String> = emptyList(),
    val pollVotesMap: Map<String, Int> = emptyMap(),
    val topicId: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val upvotesCount: Int = 0,
    val downvotesCount: Int = 0,
    val savesCount: Int = 0,
    val sharesCount: Int = 0,
    val reportsCount: Int = 0,
    val hidesCount: Int = 0,
    val engagementSecondsTotal: Long = 0,
    val qualityScore: Double = 0.0,
    val risingScore: Double = 0.0,
    val controversialScore: Double = 0.0,
    val lastScoreUpdate: Timestamp = Timestamp.now(),
    val pinnedCommentId: String? = null,
    val moderationFlags: ModerationFlags = ModerationFlags()
)

data class Comment(
    val commentId: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorHandle: String = "",
    val parentCommentId: String? = null,
    val text: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val upvotesCount: Int = 0,
    val downvotesCount: Int = 0,
    val reportsCount: Int = 0,
    val hidesCount: Int = 0,
    val engagementSecondsTotal: Long = 0,
    val qualityScore: Double = 0.0,
    val risingScore: Double = 0.0,
    val controversialScore: Double = 0.0,
    val lastScoreUpdate: Timestamp = Timestamp.now(),
    val moderationFlags: ModerationFlags = ModerationFlags()
)

data class ModerationFlags(
    val nsfw: Boolean = false,
    val spam: Boolean = false,
    val abusive: Boolean = false
)

data class ReputationLog(
    val logId: String = "",
    val userId: String = "",
    val reason: String = "",
    val delta: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val relatedPostId: String? = null,
    val relatedCommentId: String? = null
)

data class EngagementEvent(
    val eventId: String = "",
    val userId: String = "",
    val targetType: VoteTarget = VoteTarget.POST,
    val targetId: String = "",
    val dwellSeconds: Int = 0,
    val scrollDepth: Double = 0.0,
    val completionRate: Double = 0.0,
    val rereads: Int = 0,
    val createdAt: Timestamp = Timestamp.now()
)

data class ModerationQueueItem(
    val queueId: String = "",
    val targetType: VoteTarget = VoteTarget.POST,
    val targetId: String = "",
    val reporterId: String = "",
    val reason: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val status: String = "open"
)

data class Vote(
    val voteId: String = "",
    val targetType: VoteTarget = VoteTarget.POST,
    val targetId: String = "",
    val voterId: String = "",
    val voteValue: Int = 0,
    val voteWeight: Double = 1.0,
    val createdAt: Timestamp = Timestamp.now(),
    val deviceHash: String? = null,
    val isSuspicious: Boolean = false
)

data class Topic(
    val topicId: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val postCount: Int = 0,
    val trendingScore: Double = 0.0
)

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val readBy: List<String> = emptyList()
)
