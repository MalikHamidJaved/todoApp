package com.skipper.taskManager.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.skipper.taskManager.data.model.Task


@Composable
fun TaskItem(task: Task, onClick: () -> Unit, modifier: Modifier = Modifier, isDragging: Boolean) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(if (isDragging) 8.dp else 0.dp)
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
