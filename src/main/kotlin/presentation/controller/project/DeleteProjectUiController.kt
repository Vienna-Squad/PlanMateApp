package org.example.presentation.controller.project

import org.example.domain.PlanMateAppException
import org.example.domain.usecase.project.DeleteProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteProjectUiController(
    private val deleteProjectUseCase: DeleteProjectUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val exceptionViewer: ItemViewer<PlanMateAppException> = ExceptionViewer(),
) : UiController {
    override fun execute() {
        try {
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            deleteProjectUseCase(
                UUID.fromString( projectId))
            stringViewer.view("the project $projectId has been deleted.")
        } catch (exception: PlanMateAppException) {
            exceptionViewer.view(exception)
        }
    }
}