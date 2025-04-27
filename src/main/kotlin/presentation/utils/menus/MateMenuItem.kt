package org.example.presentation.utils.menus

import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

enum class MateMenuItem(val title: String, val uiController: UiController = SoonUiController()) {
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