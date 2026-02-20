package com.tagsnap.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tagsnap.data.models.PostType
import com.tagsnap.viewmodel.CreatePostViewModel

@Composable
fun CreatePostScreen(navController: NavController, viewModel: CreatePostViewModel = viewModel()) {
    val state by viewModel.state
    var mediaUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create Post", style = MaterialTheme.typography.displaySmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostType.values().forEach { type ->
                val isSelected = state.type == type
                Text(
                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .clickable { viewModel.updateType(type) }
                )
            }
        }
        OutlinedTextField(
            value = state.text,
            onValueChange = viewModel::updateText,
            label = { Text("Write your post") },
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(visible = state.type == PostType.IMAGE || state.type == PostType.VIDEO || state.type == PostType.AUDIO || state.type == PostType.CAROUSEL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = mediaUrl,
                    onValueChange = { mediaUrl = it },
                    label = { Text("Paste media URL") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        viewModel.addMediaUrl(mediaUrl)
                        mediaUrl = ""
                    }) { Text("Add media") }
                    if (state.mediaUrls.isNotEmpty()) {
                        Button(onClick = { viewModel.removeMediaUrl(state.mediaUrls.lastIndex) }) {
                            Text("Remove last")
                        }
                    }
                }
                Text(
                    text = "Media slots: ${state.mediaUrls.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        AnimatedVisibility(visible = state.type == PostType.POLL) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.pollOptions.forEachIndexed { index, option ->
                    OutlinedTextField(
                        value = option,
                        onValueChange = { viewModel.updatePollOption(index, it) },
                        label = { Text("Option ${index + 1}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.addPollOption() }) { Text("Add Option") }
                    if (state.pollOptions.size > 2) {
                        Button(onClick = { viewModel.removePollOption(state.pollOptions.size - 1) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = state.type == PostType.TEXT) {
            Text("Text-only posts still compete on engagement quality.", style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = { viewModel.submit() }, modifier = Modifier.width(160.dp)) { Text("Publish") }
        if (state.success) {
            Text("Post published!", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
