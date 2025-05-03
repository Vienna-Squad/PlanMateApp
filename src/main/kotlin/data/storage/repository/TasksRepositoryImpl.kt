package org.example.data.storage.repository

import org.example.data.storage.TaskCsvStorage
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.UUID

class TasksRepositoryImpl(
    private val storage: TaskCsvStorage
) : TasksRepository {

    override fun getTaskById(taskId: UUID): Result<Task> {
        return runCatching {
            storage.read().find { it.id == taskId }
                ?: throw NotFoundException( "Task not found")
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getAllTasks(): Result<List<Task>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun addTask(task: Task): Result<Unit> {
        return runCatching {
            storage.append(task)
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun updateTask(task: Task): Result<Unit> {
        return runCatching {
            val tasks = storage.read().toMutableList()
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                storage.write(tasks)
            } else {
                throw NotFoundException( "Task not found")
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun deleteTaskById(taskId: UUID): Result<Unit> {
        return runCatching {
            val tasks = storage.read().toMutableList()
            val removed = tasks.removeIf { it.id == taskId }
            if (removed) {
                storage.write(tasks)
            } else {
                throw NotFoundException( "Task not found")
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }
}