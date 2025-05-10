package org.example.presentation.controller.project

import org.example.domain.entity.Project
import org.example.domain.usecase.project.GetAllProjectsUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.ItemsViewer
import org.example.presentation.utils.viewer.ProjectsViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.mp.KoinPlatform.getKoin

class GetAllProjectsUiController(
    private val getAllProjectsUseCase: GetAllProjectsUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val projectsViewer: ItemsViewer<Project> = ProjectsViewer(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            getAllProjectsUseCase().let {
                viewer.view("All projects created by you.\n")
                projectsViewer.view(it)
            }
        }
    }
}