package org.example.data.repository

import org.example.data.datasource.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*

class TasksRepositoryImpl(
    private val localDataSource: DataSource<Task>,
    private val remoteDataSource: DataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = safeCall {
        if (isRemote()) {
            remoteDataSource.getItemById(taskId)
        } else {
            localDataSource.getItemById(taskId)
        }
    }

    override fun getAllTasks() = safeCall {
        if (isRemote()) {
            remoteDataSource.getAllItems()
        } else {
            localDataSource.getAllItems()
        }
    }

    override fun addTask(newTask: Task) = safeCall {
        if (isRemote()) {
            remoteDataSource.addItem(newTask)
        } else {
            localDataSource.addItem(newTask)
        }
    }

    override fun updateTask(updatedTask: Task) = safeCall {
        if (isRemote()) {
            remoteDataSource.updateItem(updatedTask)
        } else {
            localDataSource.updateItem(updatedTask)
        }
    }

    override fun deleteTaskById(taskId: UUID) = safeCall {
        if (isRemote()) {
            remoteDataSource.deleteItem(getTaskById(taskId))
        } else {
            localDataSource.deleteItem(getTaskById(taskId))
        }
    }
}