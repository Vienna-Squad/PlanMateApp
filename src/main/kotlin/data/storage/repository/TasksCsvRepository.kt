package org.example.data.storage.repository

import org.example.data.storage.TaskCsvStorage
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class TasksCsvRepository (
    private val storage: TaskCsvStorage
) : TasksRepository {
    override fun get(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun add(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun update(task: Task): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(taskId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}