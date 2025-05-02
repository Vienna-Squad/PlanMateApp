package org.example.domain.repository

import org.example.domain.entity.Task
import java.util.*

interface TasksRepository {
    fun getTaskById(taskId: UUID): Result<Task>
    fun getAllTasks(): Result<List<Task>>
    fun addTask(task: Task): Result<Unit>
    fun updateTask(task: Task): Result<Unit>
    fun deleteTaskById(taskId: UUID): Result<Unit>
}