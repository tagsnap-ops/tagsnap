package com.tagsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tagsnap.ui.navigation.NavRoutes
import com.tagsnap.viewmodel.AuthViewModel

@Composable
fun AuthScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Back", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.login(email, password)
            navController.navigate(NavRoutes.Home.route)
        }) { Text("Login") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate(NavRoutes.Register.route) }) { Text("Create Account") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate(NavRoutes.ForgotPassword.route) }) { Text("Forgot Password") }
    }
}

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = handle, onValueChange = { handle = it }, label = { Text("Handle") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.register(email, password, handle)
            navController.navigate(NavRoutes.EmailVerification.route)
        }) { Text("Register") }
    }
}

@Composable
fun EmailVerificationScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Verify your email", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { authViewModel.sendVerification() }) { Text("Resend Email") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate(NavRoutes.Home.route) }) { Text("Continue") }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reset Password", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.resetPassword(email)
            navController.navigate(NavRoutes.Login.route)
        }) { Text("Send Reset Link") }
    }
}
