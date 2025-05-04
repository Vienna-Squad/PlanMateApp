package org.example.presentation.controller.project


import domain.usecase.project.DeleteStateFromProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class DeleteStateFromProjectUiController(
    private val deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Enter project id: ")
            val projectId = inputReader.getInput()
            print("Enter state you want to delete: ")
            val stateToDelete = inputReader.getInput()
            deleteStateFromProjectUseCase.invoke(
                projectId = UUID.fromString(projectId),
                state = stateToDelete
            ).onSuccess {
                viewer.view("State deleted successfully")
            }
        }
    }
}
