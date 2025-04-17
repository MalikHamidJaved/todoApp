package com.skipper.taskManager.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skipper.taskManager.data.model.Task
import com.skipper.taskManager.di.AppModule
import com.skipper.taskManager.ui.viewModel.TaskViewModel
import com.skipper.taskManager.ui.viewModel.TaskViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navController: NavController,
    taskId: Long
) {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(AppModule.repository))
    var task by remember { mutableStateOf<Task?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(taskId) {
        task = viewModel.getTask(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Details") })
        }
    ) { padding ->
        task?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = it.title, style = MaterialTheme.typography.headlineSmall)
                it.description?.let { desc ->
                    Text(text = desc, style = MaterialTheme.typography.bodyLarge)
                }
                Text(text = "Priority: ${it.priority}")
                it.dueDate?.let { due ->
                    Text(text = "Due: ${Date(due).toLocaleString().substring(0, 11)}")
                }
                Text(text = "Completed: ${it.isCompleted}")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.updateTask(it.copy(isCompleted = true))
                                navController.popBackStack()
                            }
                        },
                        enabled = !it.isCompleted
                    ) {
                        Text("Mark Complete")
                    }

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.deleteTask(it)
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
