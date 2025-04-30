package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.EditProjectNameUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewerDemo
import org.example.presentation.utils.viewer.ItemDetailsViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin

class EditProjectNameUiController(
    private val editProjectNameUseCase: EditProjectNameUseCase = getKoin().get(),
    private val stringViewer: ItemDetailsViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor(),
    private val exceptionViewer: ItemDetailsViewer<PlanMateAppException> = ExceptionViewerDemo(),
) : UiController {
    override fun execute() {
        try {
            print("enter project ID: ")
            val projectId = interactor.getInput()
            print("enter the new project name: ")
            val newProjectName = interactor.getInput()
            editProjectNameUseCase(projectId, newProjectName)
            stringViewer.view("the project $projectId's name has been updated to newProjectName.")
        } catch (exception: PlanMateAppException) {
            exceptionViewer.view(exception)
        }
    }
}