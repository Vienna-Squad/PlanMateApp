package org.example.presentation.utils.menus

import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

enum class AdminMenuItem(val title: String, val uiController: UiController = SoonUiController()) {
    CREATE_PROJECT(""),
    EDIT_PROJECT_NAME(""),
    ADD_STATE_TO_PROJECT(""),
    DELETE_STATE_FROM_PROJECT(""),
    ADD_MATE_TO_PROJECT(""),
    DELETE_MATE_FROM_PROJECT(""),
    DELETE_PROJECT(""),
    GET_ALL_TASKS_OF_PROJECT(""),
    GET_PROJECT_HISTORY(""),

    CREATE_TASK(""),
    DELETE_TASK(""),
    EDIT_TASK(""),
    GET_TASK(""),
    GET_TASK_HISTORY(""),

    LOG_OUT("");

    fun execute() = this.uiController.execute()
}