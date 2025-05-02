//package org.example.presentation.utils.viewer
//
//import org.example.colors
//import org.example.domain.entity.Project
//import org.example.domain.entity.Task
//import org.example.padCell
//import org.example.reset
//
//class ProjectViewer:ItemsViewer<Project> {
//    override fun view(projects: List<Project>,tasks: List<Task>) {
//       printSwimlanes(projects,tasks)
//    }
//    fun printSwimlanes(projects: List<Project>, tasks: List<Task>) {
//        val columnWidth = 20
//        val maxTasks = projects.maxOf { project -> tasks.count { it.projectId == project.id } }
//
//        println("Welcome to PlanMate App - Project Task Swimlanes")
//        println("=".repeat(projects.size * (columnWidth + 3)))
//
//        // Header
//        print("|")
//        projects.forEachIndexed { i, project ->
//            val paddedName = padCell(project.name, columnWidth)
//            val color = colors[i % colors.size]
//            print("$color $paddedName $reset|")
//        }
//        println()
//        println("-".repeat(projects.size * (columnWidth + 3)))
//
//        // Tasks rows
//        for (row in 0 until maxTasks) {
//            print("|")
//            projects.forEachIndexed { i, project ->
//                val color = colors[i % colors.size]
//                val projectTasks = tasks.filter { it.projectId == project.id }
//                val taskTitle = if (row < projectTasks.size) projectTasks[row].title else ""
//                val paddedTask = padCell(taskTitle, columnWidth)
//                print("$color $paddedTask $reset|")
//            }
//            println()
//        }
//
//        println("=".repeat(projects.size * (columnWidth + 3)))
//    }
//}