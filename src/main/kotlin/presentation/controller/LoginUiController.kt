package org.example.presentation.controller

import org.example.domain.NoFoundException
import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.java.KoinJavaComponent.getKoin

class LoginUiController(
    private val loginUseCase: LoginUseCase=getKoin().get() ,
    private val interactor: Interactor<String> = StringInteractor(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter username: ")
            val username = interactor.getInput()
            print("enter password: ")
            val password = interactor.getInput()
            if(username.isBlank()||password.isBlank())
                throw NoFoundException()
            loginUseCase(username, password)
        }
    }
}