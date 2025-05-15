package org.example.presentation.controller.project

import org.example.domain.exceptions.InvalidInputException
import org.example.domain.usecase.project.EditProjectNameUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin
import java.util.*

class EditProjectNameUiController(
    private val editProjectNameUseCase: EditProjectNameUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            print("Enter the new project name: ")
            val newProjectName = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            editProjectNameUseCase(UUID.fromString(projectId), newProjectName)
            viewer.view("Project #$projectId's name has been successfully updated to $newProjectName.\n")
        }
    }
}