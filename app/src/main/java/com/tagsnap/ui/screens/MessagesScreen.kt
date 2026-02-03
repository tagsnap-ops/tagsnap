package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tagsnap.viewmodel.MessagesViewModel

@Composable
fun MessagesScreen(navController: NavController, viewModel: MessagesViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.observeChat("global")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Messages", style = MaterialTheme.typography.displaySmall)
        LazyColumn(contentPadding = PaddingValues(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.messages) { message ->
                Text(text = "${message.senderId.take(6)}: ${message.text}")
            }
        }
        OutlinedTextField(value = input, onValueChange = { input = it }, label = { Text("Message") })
        Button(onClick = {
            viewModel.sendMessage("global", input)
            input = ""
        }) { Text("Send") }
    }
}
