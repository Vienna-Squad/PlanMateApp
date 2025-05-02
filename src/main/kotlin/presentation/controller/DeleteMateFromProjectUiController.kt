package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.example.presentation.utils.viewer.ExceptionViewerDemo
import org.example.presentation.utils.viewer.ItemDetailsViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin

class DeleteMateFromProjectUiController(
    private val deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase = getKoin().get(),
    private val stringViewer: ItemDetailsViewer<String> = StringViewer(),
    private val interactor: Interactor<String> = StringInteractor(),
    private val exceptionViewer: ItemDetailsViewer<PlanMateAppException> = ExceptionViewerDemo(),
) : UiController {
    override fun execute() {
        try {
            print("enter project ID: ")
            val projectId = interactor.getInput()
            print("enter mate ID: ")
            val mateId = interactor.getInput()
            deleteMateFromProjectUseCase(projectId, mateId)
            stringViewer.view("the mate $mateId has been deleted from project $projectId.")
        } catch (exception: PlanMateAppException) {
            exceptionViewer.view(exception)
        }
    }
}