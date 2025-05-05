package org.example.presentation.controller.project

import org.example.domain.InvalidInputException
import org.example.domain.usecase.project.CreateProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin

class CreateProjectUiController(
    private val createProjectUseCase: CreateProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the name of the new project: ")
            val name = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException("Project name cannot be empty. Please provide a valid name.")
            }
            createProjectUseCase(name = name)
            viewer.view("Project \"$name\" has been created successfully.\n")
        }
    }
}
