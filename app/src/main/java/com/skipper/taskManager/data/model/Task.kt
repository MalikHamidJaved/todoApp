package com.skipper.taskManager.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val isCompleted: Boolean = false
)

enum class Priority { LOW, MEDIUM, HIGH }

enum class TaskFilter { ALL, COMPLETED, PENDING }

enum class SortOption { PRIORITY, DUE_DATE, ALPHABETICAL }
