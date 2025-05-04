package org.example.presentation.controller.auth

import org.example.domain.usecase.auth.LogoutUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class LogoutUiController(
    private val logoutUseCase: LogoutUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            logoutUseCase()
                .onSuccess { viewer.view("logged out successfully!!")}
                .exceptionOrNull()
        }
    }
}