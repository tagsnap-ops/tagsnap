package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tagsnap.ui.navigation.NavRoutes
import com.tagsnap.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Profile", style = MaterialTheme.typography.displaySmall)
        Text("Handle: ${state.user?.handle ?: ""}", style = MaterialTheme.typography.bodyLarge)
        Text("Reputation: ${state.user?.reputationPoints ?: 0}", style = MaterialTheme.typography.bodyLarge)
        Text("Badges: ${state.user?.badges?.joinToString() ?: ""}", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { navController.navigate(NavRoutes.Settings.route) }) { Text("Settings") }
    }
}
