package org.example.presentation.controller.project

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetAllTasksOfProjectController(
    private val getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase = getKoin().get(),
    private val stringViewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
): UiController {
    override fun execute() {
        tryAndShowError{
            println("enter project ID: ")
            val projectId = inputReader.getInput()
            if (projectId.isBlank()) {
                throw InvalidIdException()
            }
            val tasks = getAllTasksOfProjectUseCase(
                UUID.fromString( projectId))
            stringViewer.view(tasks.toString())
        }

    }
}