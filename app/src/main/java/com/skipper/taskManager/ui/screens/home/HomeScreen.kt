package com.skipper.taskManager.ui.screens.home


import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skipper.taskManager.di.AppModule
import com.skipper.taskManager.navigation.Screen
import com.skipper.taskManager.ui.viewModel.TaskViewModel
import com.skipper.taskManager.ui.viewModel.TaskViewModelFactory
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(AppModule.repository))
    val tasks by viewModel.tasks.collectAsState()

    val mutableTasks = remember(tasks) { mutableStateListOf(*tasks.toTypedArray()) }

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(tasks) {
        mutableTasks.clear()
        mutableTasks.addAll(tasks)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Task Manager") }) },
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

                LazyColumn {
                    itemsIndexed(mutableTasks, key = { _, item -> item.id }) { index, task ->
                        val isDragging = draggedIndex == index
                        val offsetY by animateOffsetAsState(
                            targetValue = if (isDragging) dragOffset else Offset.Zero,
                            label = "dragOffsetAnimation"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset {
                                    if (isDragging) IntOffset(0, offsetY.y.roundToInt())
                                    else IntOffset.Zero
                                }
                                .zIndex(if (isDragging) 1f else 0f)
                                .pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            draggedIndex = index
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                        },
                                        onDragEnd = {
                                            draggedIndex?.let { fromIndex ->
                                                val toIndex = (fromIndex + (dragOffset.y / 150).toInt())
                                                    .coerceIn(0, mutableTasks.lastIndex)

                                                if (fromIndex != toIndex) {
                                                    val task = mutableTasks.removeAt(fromIndex)
                                                    mutableTasks.add(toIndex, task)
                                                }
                                            }
                                            draggedIndex = null
                                            dragOffset = Offset.Zero
                                        },
                                        onDragCancel = {
                                            draggedIndex = null
                                            dragOffset = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            TaskItem(
                                task = task,
                                isDragging = isDragging,
                                onClick = {
                                    navController.navigate(Screen.TaskDetails.createRoute(task.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
