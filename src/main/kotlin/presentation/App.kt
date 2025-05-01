package org.example.presentation

import org.example.presentation.controller.GetTaskUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

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
        MenuItem("Create New Project"),
        MenuItem("Edit Project Name"),
        MenuItem("Add New State to Project"),
        MenuItem("Remove State from Project"),
        MenuItem("Add Mate User to Project"),
        MenuItem("Remove Mate User from Project"),
        MenuItem("Delete Project"),
        MenuItem("View All Tasks in Project"),
        MenuItem("View Project Change History"),
        MenuItem("Create New Task"),
        MenuItem("Delete Task"),
        MenuItem("Edit Task Details", GetTaskUiController()),
        MenuItem("View Task Details"),
        MenuItem("View Task Change History"),
        MenuItem("Log Out")
    )
)

class AuthApp : App(
    menuItems = listOf(
        MenuItem("Log In"),
        MenuItem("Sign Up (Register New Account)"),
        MenuItem("Exit Application")
    )
)

class MateApp : App(
    menuItems = listOf(
        MenuItem("View All Tasks in Project"),
        MenuItem("View Project Change History"),
        MenuItem("Create New Task"),
        MenuItem("Delete Task"),
        MenuItem("Edit Task Details"),
        MenuItem("View Task Details"),
        MenuItem("View Task Change History"),
        MenuItem("Log Out")
    )
)
