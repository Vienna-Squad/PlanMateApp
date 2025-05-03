package org.example.presentation.controller.task

import org.example.domain.UnknownException
import org.example.domain.entity.Task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class CreateTaskUiController(
    private val createTaskUseCase: CreateTaskUseCase=getKoin().get(),
    private val authenticationRepository: AuthenticationRepository=getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader()

) : UiController {
    override fun execute() {
        tryAndShowError {
            println("Enter task title: ")
            val taskTitle = inputReader.getInput()
            println("Enter task state: ")
            val taskState = inputReader.getInput()
            if (taskState.isBlank()) {
                throw UnknownException(
                    "Task state cannot be blank. Please provide a valid state."
                )
            }
            println("Enter project id: ")
            val projectId = inputReader.getInput()
            val createdBy = authenticationRepository.getCurrentUser().getOrElse {
                throw UnknownException(
                    "User not authenticated. Please log in to create a task."
                )
            }
            createTaskUseCase(
                Task(
                    title = taskTitle,
                    state = taskState,
                    assignedTo = emptyList(),
                    createdBy =  createdBy.id,
                    projectId = UUID.fromString( projectId)
                )
            )
            println("Task created successfully")
        }
    }
}