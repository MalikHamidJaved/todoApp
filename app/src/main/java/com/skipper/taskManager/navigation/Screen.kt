package com.skipper.taskManager.navigation


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateTask : Screen("create_task")
    object TaskDetails : Screen("task_details/{taskId}") {
        fun createRoute(taskId: Long) = "task_details/$taskId"
    }
    object Settings : Screen("settings")
}
