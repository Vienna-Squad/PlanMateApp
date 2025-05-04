package org.example.presentation.controller.project

import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteMateFromProjectUiController(
    private val deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            print("enter mate ID: ")
            val mateId = inputReader.getInput()
            deleteMateFromProjectUseCase(
                UUID.fromString(projectId),
                UUID.fromString(mateId)
            ).onSuccess {
                viewer.view("the mate $mateId has been deleted from project $projectId.")
            }.exceptionOrNull()
        }
    }
}