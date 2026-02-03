package com.tagsnap.utils

import com.google.firebase.Timestamp
import com.tagsnap.data.models.Comment
import com.tagsnap.data.models.ModerationFlags
import com.tagsnap.data.models.Post
import com.tagsnap.data.models.PostType
import com.tagsnap.data.models.Topic

fun mapPost(data: Map<String, Any?>): Post {
    return Post(
        postId = data["postId"] as? String ?: data["id"] as? String ?: "",
        authorId = data["authorId"] as? String ?: "",
        authorHandle = data["authorHandle"] as? String ?: "",
        type = PostType.valueOf(data["type"] as? String ?: PostType.TEXT.name),
        text = data["text"] as? String ?: "",
        mediaUrls = data["mediaUrls"] as? List<String> ?: emptyList(),
        pollOptions = data["pollOptions"] as? List<String> ?: emptyList(),
        pollVotesMap = data["pollVotesMap"] as? Map<String, Int> ?: emptyMap(),
        topicId = data["topicId"] as? String,
        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
        upvotesCount = (data["upvotesCount"] as? Long)?.toInt() ?: 0,
        downvotesCount = (data["downvotesCount"] as? Long)?.toInt() ?: 0,
        savesCount = (data["savesCount"] as? Long)?.toInt() ?: 0,
        sharesCount = (data["sharesCount"] as? Long)?.toInt() ?: 0,
        reportsCount = (data["reportsCount"] as? Long)?.toInt() ?: 0,
        hidesCount = (data["hidesCount"] as? Long)?.toInt() ?: 0,
        engagementSecondsTotal = (data["engagementSecondsTotal"] as? Long) ?: 0,
        qualityScore = data["qualityScore"] as? Double ?: 0.0,
        risingScore = data["risingScore"] as? Double ?: 0.0,
        controversialScore = data["controversialScore"] as? Double ?: 0.0,
        lastScoreUpdate = data["lastScoreUpdate"] as? Timestamp ?: Timestamp.now(),
        pinnedCommentId = data["pinnedCommentId"] as? String,
        moderationFlags = mapModerationFlags(data["moderationFlags"] as? Map<String, Any?>)
    )
}

fun mapComment(data: Map<String, Any?>): Comment {
    return Comment(
        commentId = data["commentId"] as? String ?: data["id"] as? String ?: "",
        postId = data["postId"] as? String ?: "",
        authorId = data["authorId"] as? String ?: "",
        authorHandle = data["authorHandle"] as? String ?: "",
        parentCommentId = data["parentCommentId"] as? String,
        text = data["text"] as? String ?: "",
        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
        upvotesCount = (data["upvotesCount"] as? Long)?.toInt() ?: 0,
        downvotesCount = (data["downvotesCount"] as? Long)?.toInt() ?: 0,
        reportsCount = (data["reportsCount"] as? Long)?.toInt() ?: 0,
        hidesCount = (data["hidesCount"] as? Long)?.toInt() ?: 0,
        engagementSecondsTotal = (data["engagementSecondsTotal"] as? Long) ?: 0,
        qualityScore = data["qualityScore"] as? Double ?: 0.0,
        risingScore = data["risingScore"] as? Double ?: 0.0,
        controversialScore = data["controversialScore"] as? Double ?: 0.0,
        lastScoreUpdate = data["lastScoreUpdate"] as? Timestamp ?: Timestamp.now(),
        moderationFlags = mapModerationFlags(data["moderationFlags"] as? Map<String, Any?>)
    )
}

fun mapTopic(data: Map<String, Any?>): Topic {
    return Topic(
        topicId = data["topicId"] as? String ?: data["id"] as? String ?: "",
        name = data["name"] as? String ?: "",
        description = data["description"] as? String ?: "",
        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now(),
        postCount = (data["postCount"] as? Long)?.toInt() ?: 0,
        trendingScore = data["trendingScore"] as? Double ?: 0.0
    )
}

private fun mapModerationFlags(map: Map<String, Any?>?): ModerationFlags {
    return ModerationFlags(
        nsfw = map?.get("nsfw") as? Boolean ?: false,
        spam = map?.get("spam") as? Boolean ?: false,
        abusive = map?.get("abusive") as? Boolean ?: false
    )
}
