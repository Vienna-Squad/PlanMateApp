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
            inputReader.getInput()
            println("Enter task state: ")
            val taskState = inputReader.getInput()
            println("Enter project id: ")
            val projectId = inputReader.getInput()
            val createdBy = authenticationRepository.getCurrentUser().getOrElse {
                throw UnknownException()
            }
            createTaskUseCase(
                Task(
                    title = taskTitle,
                    state = taskState,
                    assignedTo = emptyList(),
                    createdBy = UUID.fromString( createdBy.username),
                    projectId = UUID.fromString( projectId)
                )
            )
        }
    }
}