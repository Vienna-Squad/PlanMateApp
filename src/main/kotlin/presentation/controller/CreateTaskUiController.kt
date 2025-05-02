package org.example.presentation.controller

import org.example.domain.UnknownException
import org.example.domain.entity.Task
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.java.KoinJavaComponent.getKoin

class CreateTaskUiController(
    private val createTaskUseCase: CreateTaskUseCase=getKoin().get(),
    private val authenticationRepository: AuthenticationRepository=getKoin().get(),
    private val interactor: Interactor<String> = StringInteractor()

) : UiController {
    override fun execute() {
        tryAndShowError {
            println("Enter task title: ")
            val taskTitle = interactor.getInput()
            interactor.getInput()
            println("Enter task state: ")
            val taskState = interactor.getInput()
            println("Enter project id: ")
            val projectId = interactor.getInput()
            val createdBy = authenticationRepository.getCurrentUser().getOrElse {
                throw UnknownException()
            }
            createTaskUseCase(
                Task(
                    title = taskTitle,
                    state = taskState,
                    assignedTo = emptyList(),
                    createdBy = createdBy.username,
                    projectId = projectId
                )
            )
        }
    }
}