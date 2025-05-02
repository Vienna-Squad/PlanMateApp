package org.example.data.storage.repository

import org.example.data.storage.TaskCsvStorage
import org.example.domain.NoFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository

class TasksCsvRepository(
    private val storage: TaskCsvStorage
) : TasksRepository {

    override fun get(taskId: String): Result<Task> {
        return runCatching {
            storage.read().find { it.id == taskId }
                ?: throw NoFoundException()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getAll(): Result<List<Task>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun add(task: Task): Result<Unit> {
        return runCatching {
            storage.append(task)
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun update(task: Task): Result<Unit> {
        return runCatching {
            val tasks = storage.read().toMutableList()
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                storage.write(tasks)
            } else {
                throw NoFoundException()
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun delete(taskId: String): Result<Unit> {
        return runCatching {
            val tasks = storage.read().toMutableList()
            val removed = tasks.removeIf { it.id == taskId }
            if (removed) {
                storage.write(tasks)
            } else {
                throw NoFoundException()
            }
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }
}