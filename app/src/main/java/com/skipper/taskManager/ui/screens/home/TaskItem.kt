package com.skipper.taskManager.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.skipper.taskManager.data.model.Task
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TaskItem(
    task: Task,
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var offsetX by remember { mutableStateOf(0f) }
    var isVisible by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut() + slideOutHorizontally { fullWidth -> fullWidth },
        enter = fadeIn(),
    ) {
        Card(
            modifier = modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(if (isDragging) 8.dp else 0.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offsetX > 200 -> {
                                    // Swipe Right - Complete
                                    isVisible = false
                                    scope.launch {
                                        onComplete()
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Marked as completed",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onComplete() // Toggle back
                                        }
                                        offsetX = 0f
                                        isVisible = true
                                    }
                                }
                                offsetX < -200 -> {
                                    // Swipe Left - Delete
                                    isVisible = false
                                    scope.launch {
                                        onDelete()
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Task deleted",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            // Handle undo outside or store deleted item state
                                            // You can create a callback like onUndoDelete(task)
                                        }
                                        offsetX = 0f
                                        isVisible = true
                                    }
                                }
                                else -> {
                                    offsetX = 0f // Not enough drag, reset
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX += dragAmount
                        }
                    )
                }
                .clickable { onClick() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                task.description?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = "Priority: ${task.priority.name}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
