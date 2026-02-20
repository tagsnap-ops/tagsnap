package com.tagsnap.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tagsnap.R
import com.tagsnap.ui.navigation.NavRoutes
import com.tagsnap.ui.screens.AuthScreen
import com.tagsnap.ui.screens.BlueprintScreen
import com.tagsnap.ui.screens.CreatePostScreen
import com.tagsnap.ui.screens.DiscoverScreen
import com.tagsnap.ui.screens.EmailVerificationScreen
import com.tagsnap.ui.screens.ForgotPasswordScreen
import com.tagsnap.ui.screens.HomeScreen
import com.tagsnap.ui.screens.MessagesScreen
import com.tagsnap.ui.screens.PostDetailsScreen
import com.tagsnap.ui.screens.ProfileScreen
import com.tagsnap.ui.screens.RegisterScreen
import com.tagsnap.ui.screens.SettingsScreen
import com.tagsnap.ui.screens.TopicDetailScreen
import com.tagsnap.ui.screens.TopicsScreen

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val items = listOf(
        NavRoutes.Home,
        NavRoutes.Discover,
        NavRoutes.CreatePost,
        NavRoutes.Messages,
        NavRoutes.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = items.any { currentRoute?.startsWith(it.route) == true }
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { route ->
                        NavigationBarItem(
                            selected = currentRoute == route.route,
                            onClick = {
                                navController.navigate(route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = bottomIcon(route)),
                                    contentDescription = route.route
                                )
                            },
                            label = { Text(route.route.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        AppNavHost(navController = navController, modifier = Modifier.padding(padding))
    }
}

private fun bottomIcon(route: NavRoutes): Int {
    return when (route) {
        NavRoutes.Home -> R.drawable.ic_home
        NavRoutes.Discover -> R.drawable.ic_discover
        NavRoutes.CreatePost -> R.drawable.ic_add
        NavRoutes.Messages -> R.drawable.ic_messages
        NavRoutes.Profile -> R.drawable.ic_profile
        else -> R.drawable.ic_home
    }
}

@Composable
private fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.Login.route, modifier = modifier) {
        composable(NavRoutes.Login.route) { AuthScreen(navController) }
        composable(NavRoutes.Register.route) { RegisterScreen(navController) }
        composable(NavRoutes.EmailVerification.route) { EmailVerificationScreen(navController) }
        composable(NavRoutes.ForgotPassword.route) { ForgotPasswordScreen(navController) }
        composable(NavRoutes.Home.route) { HomeScreen(navController) }
        composable(NavRoutes.Discover.route) { DiscoverScreen(navController) }
        composable(NavRoutes.Topics.route) { TopicsScreen(navController) }
        composable(NavRoutes.TopicDetail.route) { backStack ->
            TopicDetailScreen(navController, topicId = backStack.arguments?.getString("topicId").orEmpty())
        }
        composable(NavRoutes.CreatePost.route) { CreatePostScreen(navController) }
        composable(NavRoutes.PostDetails.route) { backStack ->
            PostDetailsScreen(navController, postId = backStack.arguments?.getString("postId").orEmpty())
        }
        composable(NavRoutes.Profile.route) { ProfileScreen(navController) }
        composable(NavRoutes.Messages.route) { MessagesScreen(navController) }
        composable(NavRoutes.Settings.route) { SettingsScreen(navController) }
        composable(NavRoutes.Blueprint.route) { BlueprintScreen(navController) }
    }
}
