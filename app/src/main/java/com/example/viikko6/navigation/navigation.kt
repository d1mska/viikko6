package com.example.vk1teht.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viikko6.View.Calendar
import com.example.viikko6.View.HomeScreen
import com.example.viikko6.viewmodel.TaskViewModel

const val ROUTE_HOME = "home"
const val ROUTE_CALENDAR = "calendar"


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel =
        viewModel(factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(LocalContext.current.applicationContext as Application))
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController, taskViewModel)
        }
        composable(route = Screen.Calendar.route) {
            Calendar(navController, taskViewModel)
        }
    }
}