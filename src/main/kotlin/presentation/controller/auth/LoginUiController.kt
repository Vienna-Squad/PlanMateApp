package org.example.presentation.controller.auth

import org.example.common.Constants
import org.example.domain.NotFoundException
import org.example.domain.entity.UserRole
import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.App
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class LoginUiController(
    private val loginUseCase: LoginUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val mateApp: App = getKoin().get(named(Constants.APPS.MATE_APP)),
    private val adminApp: App = getKoin().get(named(Constants.APPS.ADMIN_APP)),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter username: ")
            val username = inputReader.getInput()
            print("enter password: ")
            val password = inputReader.getInput()
            if (username.isBlank() || password.isBlank()) NotFoundException("Username or password cannot be empty!")
            loginUseCase(username, password)
                .onSuccess { userRole ->
                    viewer.view("logged in successfully!!")
                    if (userRole == UserRole.ADMIN) {
                        adminApp.run()
                    } else if (userRole == UserRole.MATE) {
                        mateApp.run()
                    }
                }.exceptionOrNull()
        }
    }
}

