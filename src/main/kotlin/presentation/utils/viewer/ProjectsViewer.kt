package org.example.presentation.utils.viewer

import org.example.domain.entity.Project

class ProjectsViewer : ItemsViewer<Project> {
    override fun view(items: List<Project>) {
        items.forEach { project ->
            println("$project")
            println("------------------------------------------------------")
        }
    }
}