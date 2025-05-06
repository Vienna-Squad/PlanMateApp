package org.example.data.repository


import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin
import java.util.*


class TasksRepositoryImpl(
    private val tasksRemoteDataSource: RemoteDataSource<Task>,
    private val tasksLocalDataSource: LocalDataSource<Task>,
    private val preferences: Preference
) : TasksRepository {

    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            task
        } ?: throw NotFoundException("task")
    }

    override fun getAllTasks() =
        authSafeCall { tasksRemoteDataSource.getAll().ifEmpty { throw NotFoundException("tasks") } }

    override fun addTask(title: String, state: String, projectId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.add(
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
        tasksRemoteDataSource.getAll().find { it.id == task.id }?.let { existingTask ->
            if (existingTask.createdBy != currentUser.id && currentUser.id !in existingTask.assignedTo)
                throw AccessDeniedException()
            tasksRemoteDataSource.update(task)
        } ?: throw NotFoundException("task")
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            tasksRemoteDataSource.delete(task)
        } ?: throw NotFoundException("task")
    }

    override fun addMateToTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in task.assignedTo) throw AlreadyExistException()
            tasksRemoteDataSource.update(task.copy(assignedTo = task.assignedTo + mateId))
        } ?: throw NotFoundException("task")
    }

    override fun deleteMateFromTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            val assignedTo = task.assignedTo.toMutableList()
            val removed = assignedTo.remove(mateId)
            if (!removed) throw NotFoundException("mate")
            tasksRemoteDataSource.update(task.copy(assignedTo = assignedTo))
        } ?: throw NotFoundException("task")
    }

    override fun editTask(taskId: UUID, updatedTask: Task) = authSafeCall { currentUser ->
        tasksRemoteDataSource.getAll().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            tasksRemoteDataSource.update(updatedTask)
        } ?: throw NotFoundException("task")
    }
}