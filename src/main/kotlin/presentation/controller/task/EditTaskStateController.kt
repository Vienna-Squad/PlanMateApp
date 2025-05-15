package org.example.presentation.controller.task

import org.example.domain.exceptions.InvalidInputException
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class EditTaskStateController(
    private val editTaskStateUseCase: EditTaskStateUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the new state: ")
            val newState = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            editTaskStateUseCase(UUID.fromString(taskId), newState)
            viewer.view("Task #$taskId state has been successfully updated to \"$newState\".\n")
        }

    }
}