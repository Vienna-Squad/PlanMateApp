package org.example.domain.repository

import org.example.domain.entity.Task
import java.util.*

interface TasksRepository {
    fun getTaskById(taskId: UUID): Result<Task>
    fun getAllTasks(): Result<List<Task>>
    fun addTask(title: String, state: String, projectId: UUID): Result<Unit>
    fun updateTask(task: Task): Result<Unit>
    fun deleteTaskById(taskId: UUID): Result<Unit>
    fun addMateToTask(taskId: UUID,mateId: UUID): Result<Unit>
    fun deleteMateFromTask(taskId: UUID,mateId: UUID): Result<Unit>
    fun editTask(taskId: UUID, updatedTask: Task): Result<Unit>
}