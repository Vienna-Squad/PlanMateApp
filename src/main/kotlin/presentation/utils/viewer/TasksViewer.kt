package org.example.presentation.utils.viewer

import org.example.domain.entity.Task

class TasksViewer : ItemsViewer<Task> {
    override fun view(items: List<Task>) {
        items.forEach { task ->
            println("$task")
            println("------------------------------------------------------")
        }
    }
}