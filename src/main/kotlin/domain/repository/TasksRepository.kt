package org.example.domain.repository

import org.example.domain.entity.Task

interface TasksRepository {
    fun get(taskId: String): Result<Task>
    fun getAll(): Result<List<Task>>
    fun add(task: Task): Result<Unit>
    fun update(task: Task): Result<Unit>
    fun delete(taskId: String): Result<Unit>
}