package com.skipper.taskManager.data.repo

import com.skipper.taskManager.data.db.TaskDao
import com.skipper.taskManager.data.model.Task
import kotlinx.coroutines.flow.Flow

open class TaskRepository(private val taskDao: TaskDao) {
    open val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    open suspend fun insert(task: Task) = taskDao.insert(task)
    open suspend fun delete(task: Task) = taskDao.delete(task)
    open suspend fun update(task: Task) = taskDao.update(task)
    open suspend fun getTask(id: Long): Task? = taskDao.getTaskById(id)
}
