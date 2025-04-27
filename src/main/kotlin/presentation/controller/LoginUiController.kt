package org.example.presentation.controller

import org.example.presentation.AdminApp
import org.example.presentation.MateApp

class LoginUiController: UiController {
    override fun execute() {
        val username = ""
        val password = ""

        //success admin
        AdminApp().run()

        //success mate
        MateApp().run()
    }
}