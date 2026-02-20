package com.tagsnap.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tagsnap.ui.components.CommentCard
import com.tagsnap.viewmodel.PostDetailsViewModel
import com.tagsnap.data.models.Comment
import com.google.firebase.Timestamp

@Composable
fun PostDetailsScreen(navController: NavController, postId: String, viewModel: PostDetailsViewModel = viewModel()) {
    val tabs = listOf("Top", "New", "Rising", "Controversial")
    var selectedTab by remember { mutableIntStateOf(0) }
    val pinnedComment = remember {
        Comment(
            commentId = "pinned",
            postId = postId,
            authorHandle = "quality_moderator",
            text = "This comment earned the highest quality score and is auto-pinned to keep the best insight visible.",
            qualityScore = 92.4
        )
    }
    val thread = remember { sampleThread(postId) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Post Details", style = MaterialTheme.typography.displaySmall)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(12.dp)
        ) {
            Text("Pinned comment is the top quality comment.", style = MaterialTheme.typography.bodyMedium)
            Text("Threaded replies can be collapsed for focus.", style = MaterialTheme.typography.bodyMedium)
        }
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
            }
        }
        val flow = when (selectedTab) {
            0 -> viewModel.topComments(postId)
            1 -> viewModel.newComments(postId)
            2 -> viewModel.risingComments(postId)
            else -> viewModel.controversialComments(postId)
        }
        val comments = flow.collectAsLazyPagingItems()
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                CommentCard(
                    comment = pinnedComment,
                    isPinned = true
                )
            }
            items(thread) { item ->
                ThreadedComment(item = item)
            }
            items(comments) { comment ->
                if (comment != null) {
                    CommentCard(comment = comment)
                }
            }
        }
    }
}

@Composable
private fun ThreadedComment(item: ThreadedComment, depth: Int = 0) {
    var expanded by remember { mutableStateOf(true) }
    Column(modifier = Modifier.animateContentSize()) {
        CommentCard(
            comment = item.comment,
            depth = depth,
            replyCount = item.replies.size,
            expanded = expanded,
            onToggleReplies = { expanded = !expanded }
        )
        AnimatedVisibility(visible = expanded) {
            Column {
                item.replies.forEach { reply ->
                    ThreadedComment(item = reply, depth = depth + 1)
                }
            }
        }
    }
}

private data class ThreadedComment(
    val comment: Comment,
    val replies: List<ThreadedComment>
)

private fun sampleThread(postId: String): List<ThreadedComment> {
    val baseTime = Timestamp.now()
    return listOf(
        ThreadedComment(
            comment = Comment(
                commentId = "c1",
                postId = postId,
                authorHandle = "synthwave_ai",
                text = "Quality signals should weight dwell time and completion rate together to avoid clickbait.",
                qualityScore = 78.2,
                createdAt = baseTime
            ),
            replies = listOf(
                ThreadedComment(
                    comment = Comment(
                        commentId = "c1r1",
                        postId = postId,
                        authorHandle = "ux_lab",
                        text = "Agree. Also add re-read boosts so thoughtful essays can surface.",
                        qualityScore = 61.9,
                        createdAt = baseTime
                    ),
                    replies = emptyList()
                )
            )
        ),
        ThreadedComment(
            comment = Comment(
                commentId = "c2",
                postId = postId,
                authorHandle = "mod_skyline",
                text = "Auto-pin keeps the best reply visible without manual moderation.",
                qualityScore = 70.0,
                createdAt = baseTime
            ),
            replies = listOf(
                ThreadedComment(
                    comment = Comment(
                        commentId = "c2r1",
                        postId = postId,
                        authorHandle = "trust_engine",
                        text = "And it stays pinned only while it keeps earning engagement.",
                        qualityScore = 55.3,
                        createdAt = baseTime
                    ),
                    replies = listOf(
                        ThreadedComment(
                            comment = Comment(
                                commentId = "c2r1a",
                                postId = postId,
                                authorHandle = "signal_ops",
                                text = "This prevents early brigading from locking-in a bad top comment.",
                                qualityScore = 49.7,
                                createdAt = baseTime
                            ),
                            replies = emptyList()
                        )
                    )
                )
            )
        )
    )
}
