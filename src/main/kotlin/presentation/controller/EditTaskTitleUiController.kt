package org.example.presentation.controller

import org.example.domain.entity.Task
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer

class EditTaskTitleUiController(
    private val editTaskTitleUseCase: EditTaskTitleUseCase,
    private val interactor: Interactor<String> = StringInteractor(),
    private val itemViewer: ItemViewer<String> = StringViewer(),
): UiController {
    override fun execute() {
        tryAndShowError {
            itemViewer.view("Enter The New Title : ")
            val title = interactor.getInput()
            itemViewer.view("Enter The Title Id  : ")
            val taskId = interactor.getInput()
            editTaskTitleUseCase.invoke(
                taskId = taskId,
                title = title
            )
        }
    }
}