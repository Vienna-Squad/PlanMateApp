package org.example.data.repository

import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import java.util.*


import org.example.data.datasource.mongo.MongoPreferences
import org.example.data.datasource.mongo.TasksMongoStorage


class TasksRepositoryImpl(
    private val tasksStorage: TasksMongoStorage,
    private val preferences: MongoPreferences
) : TasksRepository {

    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksStorage.findByTaskId(taskId)?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            task
        } ?: throw NotFoundException("task")
    }

    override fun getAllTasks() =
        authSafeCall { tasksStorage.getAll().ifEmpty { throw NotFoundException("tasks") } }

    override fun addTask(title: String, state: String, projectId: UUID) = authSafeCall { currentUser ->
        tasksStorage.add(
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
        tasksStorage.findByTaskId(task.id)?.let { existingTask ->
            if (existingTask.createdBy != currentUser.id && currentUser.id !in existingTask.assignedTo)
                throw AccessDeniedException()
            tasksStorage.update(task)
        } ?: throw NotFoundException("task")
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksStorage.findByTaskId(taskId)?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            tasksStorage.delete(task)
        } ?: throw NotFoundException("task")
    }

    override fun addMateToTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksStorage.findByTaskId(taskId)?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in task.assignedTo) throw AlreadyExistException()
            tasksStorage.update(task.copy(assignedTo = task.assignedTo + mateId))
        } ?: throw NotFoundException("task")
    }

    override fun deleteMateFromTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksStorage.findByTaskId(taskId)?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            val assignedTo = task.assignedTo.toMutableList()
            val removed = assignedTo.remove(mateId)
            if (!removed) throw NotFoundException("mate")
            tasksStorage.update(task.copy(assignedTo = assignedTo))
        } ?: throw NotFoundException("task")
    }

    override fun editTask(taskId: UUID, updatedTask: Task) = authSafeCall { currentUser ->
        tasksStorage.findByTaskId(taskId)?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo)
                throw AccessDeniedException()
            tasksStorage.update(updatedTask)
        } ?: throw NotFoundException("task")
    }
}