package org.example.presentation.controller.task

import org.example.domain.UnknownException
import org.example.domain.usecase.task.CreateTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class CreateTaskUiController(
    private val createTaskUseCase: CreateTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Enter task title: ")
            val taskTitle = inputReader.getInput()
            print("Enter task state: ")
            val taskState = inputReader.getInput()
            if (taskState.isBlank()) throw UnknownException("Task state cannot be blank. Please provide a valid state.")
            print("Enter project id: ")
            val projectId = inputReader.getInput()
            createTaskUseCase(
                title = taskTitle,
                state = taskState,
                projectId = UUID.fromString(projectId)
            ).onSuccess {
                viewer.view("Task created successfully")
            }.exceptionOrNull()
        }
    }
}