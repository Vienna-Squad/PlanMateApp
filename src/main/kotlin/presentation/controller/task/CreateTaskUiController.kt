package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.usecase.task.CreateTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class CreateTaskUiController(
    private val createTaskUseCase: CreateTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the task title: ")
            val taskTitle = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the task state: ")
            val taskState = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            createTaskUseCase(
                title = taskTitle,
                stateName = taskState,
                projectId = UUID.fromString(projectId)
            )
            viewer.view("Task \"$taskTitle\" has been created successfully under project [$projectId].\n")
        }
    }
}