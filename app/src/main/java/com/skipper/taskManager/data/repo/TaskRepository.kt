package com.skipper.taskManager.data.repo

import com.skipper.taskManager.data.db.TaskDao
import com.skipper.taskManager.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) = taskDao.insert(task)
    suspend fun delete(task: Task) = taskDao.delete(task)
    suspend fun update(task: Task) = taskDao.update(task)
    suspend fun getTask(id: Long): Task? = taskDao.getTaskById(id)
}
