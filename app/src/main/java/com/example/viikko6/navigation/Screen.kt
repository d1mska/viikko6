package com.example.vk1teht.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
}