package org.example.data.repository

import org.example.data.datasource.csv.TasksCsvStorage
import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.entity.Task
import org.example.data.repository.Repository
import org.example.domain.AlreadyExistException
import org.example.domain.repository.TasksRepository
import java.util.UUID

class TasksRepositoryImpl(
    private val tasksCsvStorage: TasksCsvStorage
) : Repository(), TasksRepository {
    override fun getTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksCsvStorage.read().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            task
        } ?: throw NotFoundException("task")
    }

    override fun getAllTasks() = safeCall { tasksCsvStorage.read().ifEmpty { throw NotFoundException("tasks") } }

    override fun addTask(title: String, state: String, projectId: UUID) = authSafeCall { currentUser ->
        tasksCsvStorage.append(
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
        tasksCsvStorage.read().find { it.id == task.id }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksCsvStorage.updateItem(task)
        } ?: throw NotFoundException("task")
    }

    override fun deleteTaskById(taskId: UUID) = authSafeCall { currentUser ->
        tasksCsvStorage.read().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksCsvStorage.deleteItem(task)
        } ?: throw NotFoundException("task")
    }

    override fun addMateToTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksCsvStorage.read().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            if (mateId in task.assignedTo) throw AlreadyExistException()
            tasksCsvStorage.updateItem(task.copy(assignedTo = task.assignedTo + mateId))
        } ?: throw NotFoundException("task")
    }

    override fun deleteMateFromTask(taskId: UUID, mateId: UUID) = authSafeCall { currentUser ->
        tasksCsvStorage.read().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id) throw AccessDeniedException()
            val mateIndex = task.assignedTo.indexOfFirst { it == mateId }
            if (mateIndex == -1) throw NotFoundException("mate")
            val list = task.assignedTo.toMutableList()
            list.removeAt(mateIndex)
            tasksCsvStorage.updateItem(task.copy(assignedTo = list))
        } ?: throw NotFoundException("task")
    }

    override fun editTask(taskId: UUID, updatedTask: Task) = authSafeCall { currentUser ->
        tasksCsvStorage.read().find { it.id == taskId }?.let { task ->
            if (task.createdBy != currentUser.id && currentUser.id !in task.assignedTo) throw AccessDeniedException()
            tasksCsvStorage.updateItem(updatedTask)
        } ?: throw NotFoundException("task")
    }
}