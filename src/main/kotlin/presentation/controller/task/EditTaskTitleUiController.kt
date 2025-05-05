package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class EditTaskTitleUiController(
    private val editTaskTitleUseCase: EditTaskTitleUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            viewer.view("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Task ID cannot be empty. Please provide a valid ID.")
            }
            viewer.view("Please enter the new title: ")
            val title = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Title cannot be empty. Please provide a valid title.")
            }
            editTaskTitleUseCase(UUID.fromString(taskId), title)
            viewer.view("Task title has been successfully updated.\n")
        }
    }
}