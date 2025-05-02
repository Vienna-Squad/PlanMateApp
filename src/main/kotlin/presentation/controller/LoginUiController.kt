package org.example.presentation.controller

import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.UserType
import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.App
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class LoginUiController(
    private val loginUseCase: LoginUseCase=getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val mateApp: App =  getKoin().get(named("mate")),
    private val adminApp: App = getKoin().get(named("admin"))
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter username: ")
            val username = inputReader.getInput()
            print("enter password: ")
            val password = inputReader.getInput()
            if (username.isBlank() || password.isBlank())
                throw NoFoundException()
            val user = loginUseCase(username, password).getOrElse { throw UnauthorizedException() }
            when (user.type) {
                UserType.MATE -> mateApp.run()
                UserType.ADMIN -> adminApp.run()

            }
        }
    }
}

