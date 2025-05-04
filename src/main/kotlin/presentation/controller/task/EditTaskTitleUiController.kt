package org.example.presentation.controller.task

import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class EditTaskTitleUiController(
    private val editTaskTitleUseCase: EditTaskTitleUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            viewer.view("Enter The Task Id  : ")
            val taskId = inputReader.getInput()
            viewer.view("Enter The New Title : ")
            val title = inputReader.getInput()
            editTaskTitleUseCase(taskId = UUID.fromString(taskId), title = title)
                .onSuccess {
                    viewer.view("Task title updated successfully.")
                }.exceptionOrNull()
        }
    }
}