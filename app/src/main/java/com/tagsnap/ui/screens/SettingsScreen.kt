package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", style = MaterialTheme.typography.displaySmall)
        Text("Account Center", style = MaterialTheme.typography.titleMedium)
        Text("Privacy & Safety", style = MaterialTheme.typography.titleMedium)
        Text("Notifications", style = MaterialTheme.typography.titleMedium)
        Text("Appearance", style = MaterialTheme.typography.titleMedium)
        Text("Help & Support", style = MaterialTheme.typography.titleMedium)
    }
}
