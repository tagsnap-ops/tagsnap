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
