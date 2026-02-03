package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tagsnap.data.models.Topic
import com.tagsnap.ui.navigation.NavRoutes

@Composable
fun TopicsScreen(navController: NavController) {
    val topics = listOf(
        Topic(topicId = "1", name = "Tech", description = "Engineering breakthroughs"),
        Topic(topicId = "2", name = "Wellness", description = "Mind + body"),
        Topic(topicId = "3", name = "Culture", description = "Modern perspective")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Topics", style = MaterialTheme.typography.displaySmall)
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topics) { topic ->
                Text(
                    text = "${topic.name} • ${topic.description}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun TopicDetailScreen(navController: NavController, topicId: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Topic $topicId", style = MaterialTheme.typography.displaySmall)
        Text("Curated posts ranked by quality score.", style = MaterialTheme.typography.bodyLarge)
    }
}
