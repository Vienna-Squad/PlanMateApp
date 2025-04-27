package org.example.presentation.utils.menus

import org.example.presentation.controller.ExitUiController
import org.example.presentation.controller.LoginUiController
import org.example.presentation.controller.SoonUiController
import org.example.presentation.controller.UiController

enum class AuthMenuItems(val title: String, val uiController: UiController = SoonUiController()) {
    LOGIN("Log In", LoginUiController()),
    SIGN_UP("Sign Up (Register New Account)"),
    EXIT("Exit Application", ExitUiController());

    fun execute() = this.uiController.execute()
}