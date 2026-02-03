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
import com.tagsnap.ui.components.CommentCard
import com.tagsnap.viewmodel.PostDetailsViewModel

@Composable
fun PostDetailsScreen(navController: NavController, postId: String, viewModel: PostDetailsViewModel = viewModel()) {
    val tabs = listOf("Top", "New", "Rising", "Controversial")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Post Details", style = MaterialTheme.typography.displaySmall)
        Text("Pinned comment is the top quality comment.", style = MaterialTheme.typography.bodyMedium)
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
            items(comments) { comment ->
                if (comment != null) {
                    CommentCard(comment = comment)
                }
            }
        }
    }
}
