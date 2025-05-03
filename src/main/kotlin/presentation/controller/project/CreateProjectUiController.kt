package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.CreateProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class CreateProjectUiController(
    private val createProjectUseCase: CreateProjectUseCase = getKoin().get(),
    private val stringInputReader: InputReader<String> = StringInputReader(),
    private val itemViewer: ItemViewer<String> = StringViewer()
) : UiController {
    override fun execute() {
        tryAndShowError {
            println("enter name of project: ")
            val name = stringInputReader.getInput()
            if (name.isEmpty()) throw InvalidIdException(
                "Project name cannot be empty. Please provide a valid name."
            )

            createProjectUseCase(name = name)
            itemViewer.view("Project created successfully")

        }
    }
}
