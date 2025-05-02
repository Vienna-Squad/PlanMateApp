package org.example.presentation.controller.project

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteMateFromProjectUiController(
    private val deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val exceptionViewer: ItemViewer<PlanMateAppException> = ExceptionViewer(),
) : UiController {
    override fun execute() {
        try {
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            print("enter mate ID: ")
            val mateId = inputReader.getInput()
            deleteMateFromProjectUseCase(
                UUID.fromString( projectId),UUID.fromString(  mateId))
            stringViewer.view("the mate $mateId has been deleted from project $projectId.")
        } catch (exception: PlanMateAppException) {
            exceptionViewer.view(exception)
        }
    }
}