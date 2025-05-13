package org.example.domain.repository

import org.example.domain.entity.Task
import java.util.*

interface TasksRepository  {
    fun getTaskById(taskId: UUID): Task
    fun getAllTasks(): List<Task>
    fun addTask(newTask: Task)
    fun updateTask(updatedTask: Task)
    fun deleteTaskById(taskId: UUID)
}