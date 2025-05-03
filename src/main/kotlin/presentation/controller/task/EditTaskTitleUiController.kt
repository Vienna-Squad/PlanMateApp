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
    private val inputReader: InputReader<String> = StringInputReader(),
    private val itemViewer: ItemViewer<String> = StringViewer(),
): UiController {
    override fun execute() {
        tryAndShowError {
            itemViewer.view("Enter The Task Id  : ")
            val taskId = inputReader.getInput()
            itemViewer.view("Enter The New Title : ")
            val title = inputReader.getInput()
            editTaskTitleUseCase.invoke(
                taskId = UUID.fromString( taskId),
                title = title
            )
            println("Task title updated successfully.")
        }
    }
}