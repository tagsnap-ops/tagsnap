package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
        OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Search") })
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(trending) { topic ->
                Text(text = "${topic.name} • ${topic.description}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
