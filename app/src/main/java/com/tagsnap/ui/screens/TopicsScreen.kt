package com.tagsnap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        Text("Topics & Communities", style = MaterialTheme.typography.displaySmall)
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topics) { topic ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { navController.navigate(NavRoutes.TopicDetail.create(topic.topicId)) }
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = topic.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = topic.description, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Ranking mode: Top quality", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun TopicDetailScreen(navController: NavController, topicId: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Topic $topicId", style = MaterialTheme.typography.displaySmall)
        Text("Curated posts ranked by quality score.", style = MaterialTheme.typography.bodyLarge)
        Text("Auto-moderation keeps brigading and spam down.", style = MaterialTheme.typography.bodyMedium)
    }
}
