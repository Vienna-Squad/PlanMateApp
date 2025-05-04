package org.example.presentation.controller.project

import org.example.domain.usecase.project.EditProjectNameUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class EditProjectNameUiController(
    private val editProjectNameUseCase: EditProjectNameUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
    ) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter project ID: ")
            val projectId = inputReader.getInput()
            print("enter the new project name: ")
            val newProjectName = inputReader.getInput()
            tryUseCase(useCaseCall = {
                editProjectNameUseCase(
                    UUID.fromString(projectId), newProjectName
                )
            }) {
                stringViewer.view("the project $projectId's name has been updated to $newProjectName.")
            }
        }
    }
}