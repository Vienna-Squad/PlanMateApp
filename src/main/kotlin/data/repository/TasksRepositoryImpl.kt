package org.example.data.repository


import data.datasource.DataSource
import org.example.data.utils.SafeExecutor
import org.example.domain.AccessDeniedException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*


class TasksRepositoryImpl(
    private val tasksDataSource: DataSource<Task>,
    private val safeExecutor: SafeExecutor
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = safeExecutor.authCall { currentUser ->
        tasksDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            task
        }
    }

    override fun getAllTasks() = safeExecutor.authCall { tasksDataSource.getAll() }

    override fun addTask(newTask: Task) = safeExecutor.authCall { tasksDataSource.add(newTask) }

    override fun updateTask(updatedTask: Task) = safeExecutor.authCall { currentUser ->
        if (updatedTask.createdBy != currentUser.id && currentUser.id !in updatedTask.assignedTo) throw AccessDeniedException()
        tasksDataSource.update(updatedTask)
    }

    override fun deleteTaskById(taskId: UUID) = safeExecutor.authCall { currentUser ->
        tasksDataSource.getById(taskId).let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksDataSource.delete(task)
        }
    }
}