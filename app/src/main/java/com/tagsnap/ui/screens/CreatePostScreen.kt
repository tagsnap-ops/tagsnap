package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tagsnap.data.models.PostType
import com.tagsnap.viewmodel.CreatePostViewModel

@Composable
fun CreatePostScreen(navController: NavController, viewModel: CreatePostViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val state by viewModel.state

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create Post", style = MaterialTheme.typography.displaySmall)
        OutlinedTextField(value = state.text, onValueChange = viewModel::updateText, label = { Text("Text") })
        Button(onClick = { expanded = true }) { Text("Type: ${state.type.name}") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            PostType.values().forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        viewModel.updateType(type)
                        expanded = false
                    }
                )
            }
        }
        Button(onClick = { viewModel.submit() }) { Text("Publish") }
        if (state.success) {
            Text("Post published!", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
