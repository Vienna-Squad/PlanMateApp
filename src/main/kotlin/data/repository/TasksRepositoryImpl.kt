package org.example.data.repository


import data.datasource.DataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*


class TasksRepositoryImpl(
    private val tasksDataSource: DataSource<Task>,
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            task
        }
    }

    override fun getAllTasks() = authSafeCall { tasksDataSource.getAll() }

    override fun addTask(newTask: Task) = authSafeCall { tasksDataSource.add(newTask) }

    override fun updateTask(updatedTask: Task) = authSafeCall { currentUser ->
        if (updatedTask.createdBy != currentUser.id && currentUser.id !in updatedTask.assignedTo) throw AccessDeniedException()
        tasksDataSource.update(updatedTask)
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksDataSource.delete(task)
        }
    }
}