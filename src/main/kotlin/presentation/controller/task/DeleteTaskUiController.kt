package org.example.presentation.controller.task

import org.example.domain.usecase.task.DeleteTaskUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteTaskUiController(
    private val deleteTaskUseCase: DeleteTaskUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            viewer.view("enter task ID to delete: ")
            val taskId = inputReader.getInput()
            deleteTaskUseCase(
                UUID.fromString(taskId))
            viewer.view("the task #$taskId deleted.")
        }
    }
}