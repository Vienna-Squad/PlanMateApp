package org.example.data.repository

import org.example.common.bases.DataSource
import org.example.data.utils.safeCall
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksDataSource: DataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = safeCall { tasksDataSource.getItemById(taskId) }
    override fun getAllTasks() = safeCall { tasksDataSource.getAllItems() }
    override fun addTask(newTask: Task) = safeCall { tasksDataSource.addItem(newTask) }
    override fun updateTask(updatedTask: Task) = safeCall { tasksDataSource.updateItem(updatedTask) }
    override fun deleteTaskById(taskId: UUID) = safeCall { tasksDataSource.deleteItem(getTaskById(taskId)) }
}