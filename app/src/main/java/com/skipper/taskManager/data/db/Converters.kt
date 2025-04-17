package com.skipper.taskManager.data.db


import androidx.room.TypeConverter
import com.skipper.taskManager.data.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(name: String): Priority = Priority.valueOf(name)
}
