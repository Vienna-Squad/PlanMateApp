package org.example.presentation.controller.task

import org.example.domain.InvalidInputException
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class AddMateToTaskUIController(
    private val addMateToTaskUseCase: AddMateToTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the Mate ID: ")
            val mateId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            addMateToTaskUseCase(UUID.fromString(taskId), UUID.fromString(mateId))
            viewer.view("Mate [$mateId] was successfully added to Task [$taskId].\n")
        }
    }
}