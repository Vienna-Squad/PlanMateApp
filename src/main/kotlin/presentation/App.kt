


package org.example.presentation

import org.example.presentation.App.MenuItem
import org.example.presentation.controller.*
import org.example.presentation.controller.project.*
import org.example.presentation.controller.task.*

data class Category(val name: String, val menuItems: List<MenuItem>)

abstract class App(val categories: List<Category>) {
    fun run() {
        var counter = 1
        categories.forEach { category ->
            println("\n${category.name}:")
            category.menuItems.forEach { option ->
                println("${counter}. ${option.title}")
                counter++
            }
        }

        print("\nEnter your selection: ")
        val input = readln().toIntOrNull() ?: -1
        val menuItem = getMenuItemByGlobalIndex(input)
        if (menuItem != null) {
            menuItem.uiController.execute()
            run()
        }
    }

    private fun getMenuItemByGlobalIndex(input: Int): MenuItem? {
        var currentIndex = 1
        for (category in categories) {
            for (item in category.menuItems) {
                if (currentIndex == input) return item
                currentIndex++
            }
        }
        return null
    }

    data class MenuItem(val title: String, val uiController: UiController = SoonUiController())
}


class AdminApp : App(
    categories = listOf(
        Category("Project Management", listOf(
            MenuItem("Create New Project", CreateProjectUiController()),
            MenuItem("Delete Project", DeleteProjectUiController()),
            MenuItem("Edit Project Name", EditProjectNameUiController()),
            MenuItem("View Project History", GetProjectHistoryUiController()),
            MenuItem("Add Mate to Project", AddMateToProjectUiController()),
            MenuItem("Delete Mate From Project", DeleteMateFromProjectUiController()),
            MenuItem("Add State to Project", AddStateToProjectUiController()),
            MenuItem("Delete State from Project"),
            MenuItem("Remove Mate User from Project"),
        )),
        Category("Task Management", listOf(
            MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
            MenuItem("Add Mate To Task", AddMateToTaskUIController()),
            MenuItem("Create New Task", CreateTaskUiController()),
            MenuItem("Delete Mate From Task", DeleteMateFromTaskUiController()),
            MenuItem("Delete Task"),
            MenuItem("Edit Task State"),
            MenuItem("Edit Task Title ", EditTaskTitleUiController()),
            MenuItem("View Task Change History", GetTaskHistoryUIController()),
            MenuItem("View Task Details", GetTaskUiController()),
        )),
        Category("Account", listOf(
            MenuItem("Log Out", LogoutUiController())
        ))
    )
)


class AuthApp : App(
    categories = listOf(
        Category("Authentication", listOf(
            MenuItem("Log In", LoginUiController()),
            MenuItem("Sign Up (Register New Account)", RegisterUiController()),
            MenuItem("Exit Application")
        ))
    )
)


class MateApp : App(
    categories = listOf(
        Category("Project Management", listOf(
            MenuItem("View Project History", GetProjectHistoryUiController())
        )),
        Category("Task Management", listOf(
            MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
            MenuItem("Add Mate To Task", AddMateToTaskUIController()),
            MenuItem("Create New Task", CreateTaskUiController()),
            MenuItem("Delete Task", DeleteProjectUiController()), // ?? DeleteProject used for DeleteTask?
            MenuItem("Edit Task State"),
            MenuItem("View Task History", GetTaskHistoryUIController()),
            MenuItem("Edit Task Title ", EditTaskTitleUiController()),
            MenuItem("View Task Details", GetTaskUiController()),
        )),
        Category("Account", listOf(
            MenuItem("Log Out", LogoutUiController())
        ))
    )
)
//package org.example.presentation
//
//import org.example.presentation.controller.*
//
//abstract class App(val menuItems: List<MenuItem>) {
//    fun run() {
//        menuItems.forEachIndexed { index, option -> println("${index + 1}. ${option.title}") }
//        print("enter your selection: ")
//        getMenuItemByIndex(readln().toIntOrNull() ?: -1)?.let { option ->
//            option.uiController.execute()
//            run()
//        } ?: return
//    }
//
//    private fun getMenuItemByIndex(input: Int) =
//        if (input != menuItems.size) menuItems.getOrNull(input - 1) else null
//
//    data class MenuItem(val title: String, val uiController: UiController = SoonUiController())
//}
//
//class AdminApp : App(
//    menuItems = listOf(
//        MenuItem("Add Mate to Project", AddMateToProjectUiController()),
//        MenuItem("Add State to Project", AddStateToProjectUiController()),
//        MenuItem("Create New Project", CreateProjectUiController()),
//        MenuItem("Delete Mate From Project", DeleteMateFromProjectUiController()),
//        MenuItem("Delete Project", DeleteProjectUiController()),
//        MenuItem("Delete State from Project"),
//        MenuItem("Edit Project Name", EditProjectNameUiController()),
//        MenuItem("View Project History", GetProjectHistoryUiController()),
//        MenuItem("Remove State from Project"),
//        MenuItem("Remove Mate User from Project"),
//        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
//        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
//        MenuItem("Create New Task", CreateTaskUiController()),
//        MenuItem("Delete Mate From Task", DeleteMateFromTaskUiController()),
//        MenuItem("Delete Task"),
//        MenuItem("Edit Task State"),
//        MenuItem("View Task Change History", GetTaskHistoryUIController()),
//        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
//        MenuItem("View Task Details", GetTaskUiController()),
//        MenuItem("Log Out", LogoutUiController())
//
//    )
//)
//
//class AuthApp : App(
//    menuItems = listOf(
//        MenuItem("Log In", LoginUiController()),
//        MenuItem("Sign Up (Register New Account),", RegisterUiController()),
//        MenuItem("Exit Application")
//    )
//)
//
//class MateApp : App(
//    menuItems = listOf(
//        MenuItem("View Project History", GetProjectHistoryUiController()),
//        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
//        MenuItem("Add Mate To Task", AddMateToTaskUIController()),
//        MenuItem("Create New Task", CreateTaskUiController()),
//        MenuItem("Delete Task", DeleteProjectUiController()),
//        MenuItem("Edit Task State"),
//        MenuItem("View Task History", GetTaskHistoryUIController()),
//        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
//        MenuItem("View Task Details", GetTaskUiController()),
//        MenuItem("Log Out", LogoutUiController())
//    )
//)
