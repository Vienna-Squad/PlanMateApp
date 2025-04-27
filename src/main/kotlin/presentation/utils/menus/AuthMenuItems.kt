package org.example.presentation.utils.menus

import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

enum class AuthMenuItems(val title: String, val uiController: UiController = SoonUiController()) {
    LOGIN(""),
    REGISTER(""),
    EXIT("", ExitUiController());

    fun execute() = this.uiController.execute()
}