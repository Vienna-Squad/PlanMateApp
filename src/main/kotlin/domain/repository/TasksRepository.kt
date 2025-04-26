package org.example.domain.repository

import org.example.domain.entity.Task

interface TasksRepository {
    fun getTasks(): List<Task>
    fun addTask(task: Task)
}