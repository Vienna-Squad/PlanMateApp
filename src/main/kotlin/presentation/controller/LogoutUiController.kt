package org.example.presentation.controller

import org.example.domain.usecase.auth.LogoutUseCase
import org.koin.java.KoinJavaComponent.getKoin

class LogoutUiController(
    private val logoutUseCase: LogoutUseCase = getKoin().get()
): UiController {
    override fun execute() {
        print("Logout : ")
        logoutUseCase.invoke()
    }
}