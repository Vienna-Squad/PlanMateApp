package org.example.presentation.controller.auth

import org.example.common.Constants
import org.example.domain.InvalidInputException
import org.example.domain.entity.User.UserRole
import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.App
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class LoginUiController(
    private val loginUseCase: LoginUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader(),
    private val mateApp: App = getKoin().get(named(Constants.APPS.MATE_APP)),
    private val adminApp: App = getKoin().get(named(Constants.APPS.ADMIN_APP)),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the username: ")
            val username = input.getInput()
            print("Please enter the password: ")
            val password = input.getInput()

            if (username.isBlank() || password.isBlank())
                throw InvalidInputException("Username and password must not be empty.")

            loginUseCase(username, password)
            viewer.view("You have successfully logged in.\n")

            loginUseCase.getCurrentUserIfLoggedIn().role.let { role ->
                if (role == UserRole.ADMIN) {
                    adminApp.run()
                } else if (role == UserRole.MATE) {
                    mateApp.run()
                }
            }
        }
    }
}

