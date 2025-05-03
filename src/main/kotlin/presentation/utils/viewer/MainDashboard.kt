package org.example.presentation.utils.viewer

import org.example.domain.entity.Project
import org.example.domain.entity.Task
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random



val reset = "\u001B[0m"
val colorsPool = listOf(
    "\u001B[40;97m",        // Black
    "\u001B[48;5;94m\u001B[97m",   // Dark brown
    "\u001B[48;5;23m\u001B[97m",   // Dark teal
    "\u001B[48;5;52m\u001B[97m",   // Dark maroon
    "\u001B[48;5;58m\u001B[97m",   // Dark olive
)

//fun run() {
//    printSwimlanes(sampleProjects, tasks)
//}
    val sampleProjects: List<Project> = listOf(
        Project(
            name = "Project Alpha",
            states = listOf("Planning"),
            createdBy = uuid(),
            matesIds = listOf(uuid(), uuid())
        ),
        Project(name = "Beta Launch", states = listOf("Design"), createdBy = uuid(), matesIds = listOf(uuid())),
        Project(name = "Gamma Initiative", states = listOf("Research"), createdBy = uuid(), matesIds = listOf(uuid())),
        Project(name = "Delta App", states = listOf("Idea"), createdBy = uuid(), matesIds = listOf(uuid())),
        Project(name = "Epsilon Tool", states = listOf("Prototype"), createdBy = uuid(), matesIds = listOf(uuid()))
    )

    fun uuid() = UUID.randomUUID()

    fun generateDummyTasks(projects: List<Project>): List<Task> {
        return projects.flatMapIndexed { index, project ->
            val numTasks = 3 + (index % 3)
            (1..numTasks).map { i ->
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

    // Handle the case when there are no projects
    if (projects.isEmpty()) {
        println("No projects available.")
        return
    }

    // Assign random distinct colors
    val usedColors = mutableMapOf<UUID, String>()
    val availableColors = colorsPool.toMutableList()

    // Make sure we don't run out of colors
    while (availableColors.size < projects.size) {
        availableColors.addAll(colorsPool)
    }

    projects.forEach { project ->
        val colorIndex = if (availableColors.isNotEmpty())
            Random.nextInt(availableColors.size)
        else
            0

        val color = if (availableColors.isNotEmpty())
            availableColors.removeAt(colorIndex)
        else
            reset

        usedColors[project.id] = color
    }

    // Calculate column widths
    val columnWidths = projects.map { project ->
        val header = "${project.name}"
        val taskTitles = tasks.filter { it.projectId == project.id }.map { it.title }
        val maxTaskLength = taskTitles.maxOfOrNull { it.length } ?: 0
        maxOf(header.length, maxTaskLength) + 2
    }

    // Handle the case when columnWidths is empty
    if (columnWidths.isEmpty()) {
        println("No data to display.")
        return
    }

    val totalWidth = columnWidths.sum() + (3 * projects.size)

    println("=".repeat(totalWidth))

    // Header
    print("|")
    projects.forEachIndexed { i, project ->
        val color = usedColors[project.id] ?: reset
        val header = project.name
        val padded = padCell(header, columnWidths[i])
        print("$color $padded $reset|")
    }
    println()
    println("-".repeat(totalWidth))

    // Tasks
    val maxTasks = projects.maxOf { project -> tasks.count { it.projectId == project.id } }

    for (row in 0 until maxTasks) {
        print("|")
        projects.forEachIndexed { i, project ->
            val color = usedColors[project.id] ?: reset
            val projectTasks = tasks.filter { it.projectId == project.id }
            val taskTitle = if (row < projectTasks.size) projectTasks[row].title else ""
            val paddedTask = padCell(taskTitle, columnWidths[i])
            print("$color $paddedTask $reset|")
        }
        println()
    }

    println("=".repeat(totalWidth))
}


