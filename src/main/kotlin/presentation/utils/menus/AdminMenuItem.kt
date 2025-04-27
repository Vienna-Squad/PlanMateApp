package org.example.presentation.utils.menus

import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

enum class AdminMenuItem(val title: String, val uiController: UiController = SoonUiController()) {
    CREATE_PROJECT("Create New Project"),
    EDIT_PROJECT_NAME("Edit Project Name"),
    ADD_STATE_TO_PROJECT("Add New State to Project"),
    DELETE_STATE_FROM_PROJECT("Remove State from Project"),
    ADD_MATE_TO_PROJECT("Add Mate User to Project"),
    DELETE_MATE_FROM_PROJECT("Remove Mate User from Project"),
    DELETE_PROJECT("Delete Project"),

    GET_ALL_TASKS_OF_PROJECT("View All Tasks in Project"),
    GET_PROJECT_HISTORY("View Project Change History"),

    CREATE_TASK("Create New Task"),
    DELETE_TASK("Delete Task"),
    EDIT_TASK("Edit Task Details"),
    GET_TASK("View Task Details"),
    GET_TASK_HISTORY("View Task Change History"),

    LOG_OUT("Log Out");

    fun execute() = this.uiController.execute()
}