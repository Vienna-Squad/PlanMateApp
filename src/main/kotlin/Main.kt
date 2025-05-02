package org.example

import di.appModule
import di.useCasesModule
import org.example.di.dataModule
import org.example.di.repositoryModule
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.presentation.AuthApp
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    println("Hello, PlanMate!")
    startKoin { modules(appModule, useCasesModule) }
    AuthApp().run()
}

/*

val reset = "\u001B[0m"
val colors = listOf(
    "\u001B[40;97m",        // Black background, white text
    "\u001B[48;5;94m\u001B[97m",   // Dark brown
    "\u001B[48;5;23m\u001B[97m",   // Dark teal
    "\u001B[48;5;52m\u001B[97m",   // Dark maroon
    "\u001B[48;5;58m\u001B[97m"    // Dark olive
)

val sampleProjects = listOf(
    Project(name = "Project Alpha", states = listOf("Planning"), createdBy = "Ahmed", matesIds = listOf("1", "2")),
    Project(name = "Beta Launch", states = listOf("Design"), createdBy = "Sara", matesIds = listOf("3", "4")),
    Project(name = "Gamma Initiative", states = listOf("Research"), createdBy = "Omar", matesIds = listOf("5", "6")),
    Project(name = "Delta App", states = listOf("Idea"), createdBy = "Laila", matesIds = listOf("7")),
    Project(name = "Epsilon Tool", states = listOf("Prototype"), createdBy = "Yousef", matesIds = listOf("8", "9")),
)

fun generateDummyTasks(projects: List<Project>): List<Task> {
    return projects.flatMapIndexed { index, project ->
        (1..(3 + index % 3)).map { i ->
            Task(
                title = "Task $i",
                state = project.states.first(),
                assignedTo = project.matesIds.shuffled().take(1),
                createdBy = project.createdBy,
                projectId = project.id
            )
        }
    }
}

fun padCell(text: String, width: Int): String = text.padEnd(width, ' ')

fun printSwimlanes(projects: List<Project>, tasks: List<Task>) {
    println("Welcome to PlanMate App - Project Task Swimlanes")

    // STEP 1: Determine max width per column
    val columnWidths = projects.map { project ->
        val header = "${project.name} ${project.createdBy}"
        val taskTitles = tasks.filter { it.projectId == project.id }.map { it.title }
        val maxTaskLength = taskTitles.maxOfOrNull { it.length } ?: 0
        maxOf(header.length, maxTaskLength) + 2 // padding
    }

    // STEP 2: Max number of task rows
    val maxTasks = projects.maxOf { project -> tasks.count { it.projectId == project.id } }

    // STEP 3: Top border
    println("=".repeat(columnWidths.sum() + (3 * projects.size)))

    // STEP 4: Headers
    print("|")
    projects.forEachIndexed { i, project ->
        val color = colors[i % colors.size]
        val header = "${project.name} ${project.createdBy}"
        val padded = padCell(header, columnWidths[i])
        print("$color $padded $reset|")
    }
    println()

    // STEP 5: Separator
    println("-".repeat(columnWidths.sum() + (3 * projects.size)))

    // STEP 6: Task rows
    for (row in 0 until maxTasks) {
        print("|")
        projects.forEachIndexed { i, project ->
            val color = colors[i % colors.size]
            val projectTasks = tasks.filter { it.projectId == project.id }
            val taskTitle = if (row < projectTasks.size) projectTasks[row].title else ""
            val paddedTask = padCell(taskTitle, columnWidths[i])
            print("$color $paddedTask $reset|")
        }
        println()
    }

    // STEP 7: Bottom border
    println("=".repeat(columnWidths.sum() + (3 * projects.size)))
}

fun main() {
    val tasks = generateDummyTasks(sampleProjects)
    printSwimlanes(sampleProjects, tasks)
}*/
