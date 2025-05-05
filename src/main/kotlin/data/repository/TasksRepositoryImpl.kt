package org.example.data.repository

import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksRemoteStorage: RemoteDataSource<Task>,
    private val tasksLocalStorage: LocalDataSource<Task>,
    private val preferences: CsvPreferences
) : TasksRepository {
    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            task
        } ?: throw NotFoundException("task")
    }

    override fun getAllTasks() =
        authSafeCall { tasksLocalStorage.getAll().ifEmpty { throw NotFoundException("tasks") } }

    override fun addTask(title: String, state: String, projectId: UUID) = authSafeCall { currentUser ->
        tasksLocalStorage.add(
            Task(
                title = title,
                state = state,
                assignedTo = emptyList(),
                createdBy = currentUser.id,
                projectId = projectId
            )
        )
    }

    override fun updateTask(task: Task) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == task.id }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksLocalStorage.update(task)
        } ?: throw NotFoundException("task")
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksLocalStorage.delete(task)
        } ?: throw NotFoundException("task")
    }

    override fun addMateToTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in task.assignedTo) throw AlreadyExistException()
            tasksLocalStorage.update(task.copy(assignedTo = task.assignedTo + mateId))
        } ?: throw NotFoundException("task")
    }

    override fun deleteMateFromTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            val mateIndex = task.assignedTo.indexOfFirst { it == mateId }
            if (mateIndex == -1) throw NotFoundException("mate")
            val list = task.assignedTo.toMutableList()
            list.removeAt(mateIndex)
            tasksLocalStorage.update(task.copy(assignedTo = list))
        } ?: throw NotFoundException("task")
    }

    override fun editTask(taskId: UUID, updatedTask: Task) = authSafeCall { currentUser ->
        tasksLocalStorage.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksLocalStorage.update(updatedTask)
        } ?: throw NotFoundException("task")
    }
}