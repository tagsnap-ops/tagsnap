package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tagsnap.ui.components.PostCard
import com.tagsnap.viewmodel.FeedViewModel
import com.tagsnap.data.repositories.PostRepository

@Composable
fun HomeScreen(navController: NavController, feedViewModel: FeedViewModel = viewModel()) {
    val tabs = listOf("Top", "New", "Rising", "Controversial")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Home", style = MaterialTheme.typography.displaySmall)
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
                        onUpvote = { PostRepository().submitVote(post.postId, 1) },
                        onDownvote = { PostRepository().submitVote(post.postId, -1) },
                        onSave = { PostRepository().savePost(post.postId) },
                        onShare = { PostRepository().sharePost(post.postId) },
                        onReport = { PostRepository().reportPost(post.postId, "Quality concern") }
                    )
                }
            }
        }
    }
}
