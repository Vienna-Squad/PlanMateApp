package org.example.presentation.controller.project

import org.example.domain.entity.Task
import org.example.domain.exceptions.InvalidInputException
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.TasksViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

class GetAllTasksOfProjectController(
    private val getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
    private val tasksViewer: ItemsViewer<Task> = TasksViewer()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the project ID: ")
            val projectId = input.getInput().also {
                if (it.isBlank()) throw InvalidInputException()
            }
            getAllTasksOfProjectUseCase(UUID.fromString(projectId)).also {
                viewer.view("Tasks for project ID $projectId:\n")
                tasksViewer.view(it)
            }
        }
    }
}