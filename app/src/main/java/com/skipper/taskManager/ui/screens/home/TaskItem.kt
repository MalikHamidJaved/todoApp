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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.skipper.taskManager.R
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
    val context = LocalContext.current

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.delete_task)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_task)) },
            confirmButton = {
                Button(onClick = {
                    showDeleteConfirmation = false
                    isVisible = false
                    scope.launch {
                        onDelete()

                        val result = snackbarHostState.showSnackbar(
                            message = context.getString(R.string.task_deleted),
                            actionLabel = context.getString(R.string.undo),
                        )
                        if (result == SnackbarResult.ActionPerformed) {

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
                                            message = context.getString(R.string.marked_as_completed),
                                            actionLabel = context.getString(R.string.undo)
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
            TaskContent(task)
        }
    }
}

@Composable
fun TaskContent(task: Task) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (checkIconRef, contentRef) = createRefs()

        if (task.isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color(0xFF4CAF50),
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(checkIconRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }

        Column(
            modifier = Modifier.constrainAs(contentRef) {
                top.linkTo(parent.top)
                start.linkTo(
                     parent.start,
                    margin = if (task.isCompleted) 8.dp else 0.dp
                )
            }
        ) {
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
