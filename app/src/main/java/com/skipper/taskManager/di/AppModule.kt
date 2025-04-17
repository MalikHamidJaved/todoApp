package com.skipper.taskManager.di

import android.content.Context
import androidx.room.Room
import com.skipper.taskManager.data.db.TaskDatabase
import com.skipper.taskManager.data.repo.TaskRepository

object AppModule {
    lateinit var repository: TaskRepository
        private set

    fun init(context: Context) {
        val db = Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        ).build()
        repository = TaskRepository(db.taskDao())
    }
}
