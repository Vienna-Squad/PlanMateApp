package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.EditProjectNameUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemDetailsViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin

class EditProjectNameUiController(
    private val editProjectNameUseCase: EditProjectNameUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor(),

) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter project ID: ")
            val projectId = interactor.getInput()
            print("enter the new project name: ")
            val newProjectName = interactor.getInput()
            editProjectNameUseCase(projectId, newProjectName)
            stringViewer.view("the project $projectId's name has been updated to newProjectName.")
        }
    }
}