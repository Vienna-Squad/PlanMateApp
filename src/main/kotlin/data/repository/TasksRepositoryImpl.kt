package org.example.data.repository


import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*


class TasksRepositoryImpl(
    private val tasksRemoteDataSource: RemoteDataSource<Task>,
    private val tasksLocalDataSource: LocalDataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            task
        }
    }

    override fun getAllTasks() = authSafeCall { tasksRemoteDataSource.getAll() }

    override fun addTask(newTask: Task) = authSafeCall { tasksRemoteDataSource.add(newTask) }

    override fun updateTask(updatedTask: Task) = authSafeCall { currentUser ->
        if (updatedTask.createdBy != currentUser.id && currentUser.id !in updatedTask.assignedTo) throw AccessDeniedException()
        tasksRemoteDataSource.update(updatedTask)
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksRemoteDataSource.delete(task)
        }
    }
}