package com.skipper.taskManager.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteConfirmation = false
                    isVisible = false
                    scope.launch {
                        onDelete()
                        val result = snackbarHostState.showSnackbar(
                            message = "Task deleted",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            // Optionally undo deletion
                        }
                        offsetX = 0f
                        isVisible = true
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    offsetX = 0f // Reset drag
                }) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

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
                                    // Swipe Left - Ask to Delete
                                    showDeleteConfirmation = true
                                }

                                else -> {
                                    offsetX = 0f
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
