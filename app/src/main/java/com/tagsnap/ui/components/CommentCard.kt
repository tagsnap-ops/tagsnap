package com.tagsnap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tagsnap.data.models.Comment

@Composable
fun CommentCard(comment: Comment) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(text = comment.authorHandle, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Score ${comment.qualityScore}", style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = comment.text, style = MaterialTheme.typography.bodyLarge)
    }
}
