package com.skipper.taskManager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skipper.taskManager.ui.screens.create.CreateTaskScreen
import com.skipper.taskManager.ui.screens.details.TaskDetailsScreen
import com.skipper.taskManager.ui.screens.home.HomeScreen
import com.skipper.taskManager.ui.screens.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.CreateTask.route) {
            CreateTaskScreen(navController)
        }
        composable(route = Screen.TaskDetails.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            taskId?.let {
                TaskDetailsScreen(navController, it)
            }
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
