package com.tagsnap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Handle: ${state.user?.handle ?: ""}", style = MaterialTheme.typography.bodyLarge)
            Text("Reputation: ${state.user?.reputationPoints ?: 0}", style = MaterialTheme.typography.bodyLarge)
            Text("Badges: ${state.user?.badges?.joinToString() ?: "Explorer"}", style = MaterialTheme.typography.bodyMedium)
            Text("Voting weight scales with reputation for quality signals.", style = MaterialTheme.typography.bodyMedium)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Posting limits", style = MaterialTheme.typography.titleMedium)
            Text("New accounts ramp up as trust increases.", style = MaterialTheme.typography.bodyMedium)
            Text("Shadow-ban protection is active.", style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = { navController.navigate(NavRoutes.Settings.route) }) { Text("Settings") }
    }
}
