package org.example.presentation

import org.example.domain.usecase.project.DeleteStateFromProjectUseCase
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.controller.*

abstract class App(val menuItems: List<MenuItem>) {
    fun run() {
        menuItems.forEachIndexed { index, option -> println("${index + 1}. ${option.title}") }
        print("enter your selection: ")
        getMenuItemByIndex(readln().toIntOrNull() ?: -1)?.let { option ->
            option.uiController.execute()
            run()
        } ?: return
    }

    private fun getMenuItemByIndex(input: Int) =
        if (input != menuItems.size) menuItems.getOrNull(input - 1) else null

    data class MenuItem(val title: String, val uiController: UiController = SoonUiController())
}

class AdminApp : App(
    menuItems = listOf(
        MenuItem("Add Mate to Project", AddMateToProjectUiController()),
        MenuItem("Add State to Project", AddStateToProjectUiController()),
        MenuItem("Create New Project", CreateProjectUiController()),
        MenuItem("Delete Mate From Project", DeleteMateFromProjectUiController()),
        MenuItem("Delete Project", DeleteProjectUiController()),
        MenuItem("Delete State from Project"),
        MenuItem("Edit Project Name", EditProjectNameUiController()),
        MenuItem("View Project History", GetProjectHistoryUiController()),
        MenuItem("Remove State from Project"),
        MenuItem("Remove Mate User from Project"),
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
        MenuItem("Create New Task", CreateTaskUiController()),
        MenuItem("Delete Mate From Task", DeleteMateFromTaskUiController()),
        MenuItem("Delete  Task"),
        MenuItem("Edit Task State"),
        MenuItem("View Task Change History", GetTaskHistoryUIController()),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("View Task Details", GetTaskUiController()),
        MenuItem("Log Out", LogoutUiController())

    )
)

class AuthApp : App(
    menuItems = listOf(
        MenuItem("Log In", LoginUiController()),
        MenuItem("Sign Up (Register New Account),", RegisterUiController()),
        MenuItem("Exit Application")
    )
)

class MateApp : App(
    menuItems = listOf(
        MenuItem("View Project History", GetProjectHistoryUiController()),
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
        MenuItem("Create New Task", CreateTaskUiController()),
        MenuItem("Delete Task", DeleteProjectUiController()),
        MenuItem("Edit Task State"),
        MenuItem("View Task History", GetTaskHistoryUIController()),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("View Task Details", GetTaskUiController()),
        MenuItem("Log Out", LogoutUiController())
    )
)
