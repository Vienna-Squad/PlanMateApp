package org.example.data.repository

import data.datasource.DataSource
import org.example.data.utils.safeCall
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksDataSource: DataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = safeCall { tasksDataSource.getById(taskId) }
    override fun getAllTasks() = safeCall { tasksDataSource.getAll() }
    override fun addTask(newTask: Task) = safeCall { tasksDataSource.add(newTask) }
    override fun updateTask(updatedTask: Task) = safeCall { tasksDataSource.update(updatedTask) }
    override fun deleteTaskById(taskId: UUID) = safeCall { tasksDataSource.delete(getTaskById(taskId)) }
}