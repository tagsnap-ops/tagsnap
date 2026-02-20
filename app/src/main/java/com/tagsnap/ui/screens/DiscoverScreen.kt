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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tagsnap.data.models.Topic

@Composable
fun DiscoverScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val trending = listOf(
        Topic(topicId = "1", name = "Productivity", description = "Habits and systems"),
        Topic(topicId = "2", name = "Design", description = "Premium UI insights"),
        Topic(topicId = "3", name = "Audio", description = "Voice-first stories")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Discover", style = MaterialTheme.typography.displaySmall)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search for topics, creators, or posts") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Trending Quality Signals", style = MaterialTheme.typography.titleMedium)
                Text("Saves, dwell, and re-reads drive momentum.", style = MaterialTheme.typography.bodyMedium)
            }
            Text("Live", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(trending) { topic ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = topic.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = topic.description, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Curated by reputation-weighted insights", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
