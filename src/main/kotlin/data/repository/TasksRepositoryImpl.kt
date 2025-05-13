package org.example.data.repository

import org.example.common.bases.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksLocalDataSource: DataSource<Task>,
    private val tasksRemoteDataSource: DataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = safeCall {
        if (isRemote()) {
            tasksRemoteDataSource.getItemById(taskId)
        } else {
            tasksLocalDataSource.getItemById(taskId)
        }
    }

    override fun getAllTasks() = safeCall {
        if (isRemote()) {
            tasksRemoteDataSource.getAllItems()
        } else {
            tasksLocalDataSource.getAllItems()
        }
    }

    override fun addTask(newTask: Task) = safeCall {
        if (isRemote()) {
            tasksRemoteDataSource.addItem(newTask)
        } else {
            tasksLocalDataSource.addItem(newTask)
        }
    }

    override fun updateTask(updatedTask: Task) = safeCall {
        if (isRemote()) {
            tasksRemoteDataSource.updateItem(updatedTask)
        } else {
            tasksLocalDataSource.updateItem(updatedTask)
        }
    }

    override fun deleteTaskById(taskId: UUID) = safeCall {
        if (isRemote()) {
            tasksRemoteDataSource.deleteItem(getTaskById(taskId))
        } else {
            tasksLocalDataSource.deleteItem(getTaskById(taskId))
        }
    }
}