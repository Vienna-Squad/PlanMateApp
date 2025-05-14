package org.example.presentation.controller.task

import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.exceptions.InvalidInputException
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class DeleteMateFromTaskUiController(
    private val deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the Task ID: ")
            val taskId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Please enter the Mate ID to remove: ")
            val mateId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            deleteMateFromTaskUseCase(
                taskId = UUID.fromString(taskId),
                mateId = UUID.fromString(mateId)
            )
            viewer.view("Mate [$mateId] has been successfully removed from Task [$taskId].\n")
        }
    }
}