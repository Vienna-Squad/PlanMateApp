package org.example.presentation

import org.example.domain.usecase.auth.CreateUserUseCase
import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController
import org.example.presentation.controller.auth.LoginUiController
import org.example.presentation.controller.auth.LogoutUiController
import org.example.presentation.controller.auth.RegisterUiController
import org.example.presentation.controller.project.*
import org.example.presentation.controller.task.*

abstract class App(val menuItems: List<MenuItem>) {
    fun run() {
        menuItems.forEachIndexed { index, menuItem ->
            println("${index + 1}. ${menuItem.title}")
        }
        print("\nEnter your selection: ")
        val input = readln().toIntOrNull() ?: -1
        val uiController = menuItems.getOrNull(input - 1)?.uiController ?: ExitUiController()
        uiController.execute()
        if (input == menuItems.size) return
        run()
    }

    data class MenuItem(val title: String, val uiController: UiController = SoonUiController())
}

class AuthApp : App(
    menuItems = listOf(
        MenuItem("Log In", LoginUiController()),
        MenuItem("Register", RegisterUiController()),
        MenuItem("Exit Application", ExitUiController())
    )
)

class AdminApp : App(
    menuItems = listOf(
        MenuItem("Create New Project", CreateProjectUiController()),
        MenuItem("Delete Project", DeleteProjectUiController()),
        MenuItem("Edit Project Name", EditProjectNameUiController()),
        MenuItem("View Project History", GetProjectHistoryUiController()),
        MenuItem("Add Mate to Project", AddMateToProjectUiController()),
        MenuItem("Delete Mate From Project", DeleteMateFromProjectUiController()),
        MenuItem("Add State to Project", AddStateToProjectUiController()),
        MenuItem("Delete State from Project", DeleteStateFromProjectUiController()),
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
        MenuItem("Create New Task", CreateTaskUiController()),
        MenuItem("Delete Mate From Task", DeleteMateFromTaskUiController()),
        MenuItem("Delete Task", DeleteTaskUiController()),
        MenuItem("Edit Task State", EditTaskStateController()),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("View Task Change History", GetTaskHistoryUIController()),
        MenuItem("View Task Details", GetTaskUiController()),
        MenuItem("Create User", RegisterUiController()),
        MenuItem("Logout", LogoutUiController()),
    )
)


class MateApp : App(
    menuItems = listOf(
        MenuItem("View Project History", GetProjectHistoryUiController()),
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
        MenuItem("Create New Task", CreateTaskUiController()),
        MenuItem("Delete Task", DeleteTaskUiController()),
        MenuItem("Edit Task State", EditTaskStateController()),
        MenuItem("View Task History", GetTaskHistoryUIController()),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("View Task Details", GetTaskUiController()),
        MenuItem("Logout", LogoutUiController()),
    )
)
