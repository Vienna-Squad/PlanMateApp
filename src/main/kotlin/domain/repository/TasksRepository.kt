package org.example.domain.repository

import org.example.domain.entity.Task
import java.util.*

interface TasksRepository {
    fun getTaskById(taskId: UUID): Task
    fun getAllTasks(): List<Task>
    fun addTask(title: String, state: String, projectId: UUID)
    fun updateTask(task: Task)
    fun deleteTaskById(taskId: UUID)
    fun addMateToTask(taskId: UUID, mateId: UUID)
    fun deleteMateFromTask(taskId: UUID, mateId: UUID)
    fun editTask(taskId: UUID, updatedTask: Task)
}