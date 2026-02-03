package com.tagsnap.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tagsnap.data.models.Post

@Composable
fun PostCard(
    post: Post,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onReport: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = post.authorHandle, style = MaterialTheme.typography.titleMedium)
            Text(text = post.text, style = MaterialTheme.typography.bodyLarge)
            if (post.mediaUrls.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(post.mediaUrls.first()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onUpvote) { Text("Up") }
                Button(onClick = onDownvote) { Text("Down") }
                Button(onClick = onSave) { Text("Save") }
                Button(onClick = onShare) { Text("Share") }
                Button(onClick = onReport) { Text("Report") }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Score ${post.qualityScore}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
