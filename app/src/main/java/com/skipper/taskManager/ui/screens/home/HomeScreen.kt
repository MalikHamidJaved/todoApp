package com.skipper.taskManager.ui.screens.home

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skipper.taskManager.data.model.Task
import com.skipper.taskManager.di.AppModule
import com.skipper.taskManager.navigation.Screen
import com.skipper.taskManager.ui.viewModel.TaskViewModel
import com.skipper.taskManager.ui.viewModel.TaskViewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(AppModule.repository))
    val tasks by viewModel.tasks.collectAsState()
    var draggedTask by remember { mutableStateOf<Task?>(null) }
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }
    val scope = rememberCoroutineScope()
    val mutableTasks = remember(tasks) { mutableStateListOf(*tasks.toTypedArray()) }

    var itemOffset by remember { mutableStateOf(Offset.Zero) }
    var itemStartOffset by remember { mutableStateOf(Offset.Zero) }

    val animatedOffset by animateOffsetAsState(
        targetValue = itemOffset,
        label = "dragAnimation"
    )

    LaunchedEffect(tasks) {
        mutableTasks.clear()
        mutableTasks.addAll(tasks)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateTask.route) },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        if (mutableTasks.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text(
                    text = "Completed: ${mutableTasks.count { it.isCompleted }} / ${mutableTasks.size}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(16.dp)
                )

                DragAndDropColumn(items = mutableTasks, onMove = { from, to ->
                    mutableTasks.apply {
                        add(to, removeAt(from))
                    }
                    scope.launch {
                        mutableTasks.forEachIndexed { index, task ->
                            viewModel.updateTask(task.copy(id = mutableTasks[index].id))
                        }
                    }
                }) { task ->
                    TaskItem(
                        task = task,
                        onClick = {
                            navController.navigate(Screen.TaskDetails.createRoute(task.id))
                        },
                        isDragging = draggedTask == task,
                        modifier = Modifier.Draggable(
                            item = task,
                            onDragStart = { offset ->
                                itemStartOffset = offset
                            },
                            onDragChange = { offset ->
                                if (draggedTask == null) {
                                    draggedTask = task;
                                    itemStartOffset = offset;
                                }
                                draggedOffset += offset
                                itemOffset += offset - itemStartOffset
                            },
                            onDragEnd = {
                                draggedTask = null
                                draggedOffset = Offset.Zero
                                itemOffset = Offset.Zero
                                itemStartOffset = Offset.Zero
                            },
                            onOver = { }
                        )
                    )
                }

                draggedTask?.let { task ->
                    TaskItem(
                        task = task,
                        onClick = {
                            navController.navigate(Screen.TaskDetails.createRoute(task.id))
                        },
                        isDragging = true,
                        modifier = Modifier.absoluteOffset {
                            IntOffset(
                                x = animatedOffset.x.roundToInt(),
                                y = animatedOffset.y.roundToInt()
                            )
                        }
                    )
                }
            }
        }
    }
}



