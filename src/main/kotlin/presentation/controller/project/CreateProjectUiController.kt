package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.CreateProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class CreateProjectUiController(
    private val createProjectUseCase: CreateProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val stringInputReader: InputReader<String> = StringInputReader(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            println("enter name of project: ")
            val name = stringInputReader.getInput()
            if (name.isEmpty()) throw InvalidIdException(
                "Project name cannot be empty. Please provide a valid name."
            )
            createProjectUseCase(name = name)
                .onSuccess {
                    viewer.view("Project created successfully")
                }.exceptionOrNull()
        }
    }
}
