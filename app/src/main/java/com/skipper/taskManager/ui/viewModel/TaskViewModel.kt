package com.skipper.taskManager.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skipper.taskManager.data.model.Task
import com.skipper.taskManager.data.repo.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    open val tasks = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    suspend fun getTask(taskId: Long): Task? {
        return repository.getTask(taskId)
    }

}

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
