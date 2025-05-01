package org.example.presentation.controller

import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.utils.interactor.Interactor

class LoginUiController(
    private val loginUseCase: LoginUseCase,
    private val interactor: Interactor<String>,
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter username: ")
            val username = interactor.getInput()
            print("enter password: ")
            val password = interactor.getInput()
            loginUseCase(username, password)
        }
    }
}