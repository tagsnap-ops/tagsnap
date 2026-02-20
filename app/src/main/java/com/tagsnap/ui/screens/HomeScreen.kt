package com.tagsnap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tagsnap.ui.components.PostCard
import com.tagsnap.viewmodel.FeedViewModel
import com.tagsnap.data.repositories.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun HomeScreen(navController: NavController, feedViewModel: FeedViewModel = viewModel()) {
    val tabs = listOf("Top", "New", "Rising", "Controversial")
    var selectedTab by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Home", style = MaterialTheme.typography.displaySmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("UpScrolled Engine", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Content rises on quality signals, not follower count.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Live Score", style = MaterialTheme.typography.bodyMedium)
                Text("Reputation-weighted votes", style = MaterialTheme.typography.bodyMedium)
            }
        }
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
            }
        }

        val flow = when (selectedTab) {
            0 -> feedViewModel.topFeed()
            1 -> feedViewModel.newFeed()
            2 -> feedViewModel.risingFeed()
            else -> feedViewModel.controversialFeed()
        }
        val posts = flow.collectAsLazyPagingItems()

        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(posts) { post ->
                if (post != null) {
                    PostCard(
                        post = post,
                        onUpvote = { launchScoped(scope) { PostRepository().submitVote(post.postId, 1) } },
                        onDownvote = { launchScoped(scope) { PostRepository().submitVote(post.postId, -1) } },
                        onSave = { launchScoped(scope) { PostRepository().savePost(post.postId) } },
                        onShare = { launchScoped(scope) { PostRepository().sharePost(post.postId) } },
                        onReport = { launchScoped(scope) { PostRepository().reportPost(post.postId, "Quality concern") } }
                    )
                }
            }
        }
    }
}

private fun launchScoped(scope: CoroutineScope, block: suspend () -> Unit) {
    scope.launch { block() }
}
