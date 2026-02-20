package com.tagsnap.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Login : NavRoutes("login")
    data object Register : NavRoutes("register")
    data object EmailVerification : NavRoutes("email_verification")
    data object ForgotPassword : NavRoutes("forgot_password")
    data object Home : NavRoutes("home")
    data object Discover : NavRoutes("discover")
    data object Topics : NavRoutes("topics")
    data object TopicDetail : NavRoutes("topic_detail/{topicId}") {
        fun create(topicId: String) = "topic_detail/$topicId"
    }
    data object CreatePost : NavRoutes("create_post")
    data object PostDetails : NavRoutes("post_details/{postId}") {
        fun create(postId: String) = "post_details/$postId"
    }
    data object Profile : NavRoutes("profile")
    data object Messages : NavRoutes("messages")
    data object Settings : NavRoutes("settings")
    data object Blueprint : NavRoutes("blueprint")
}
