package org.example.presentation.controller.auth

import org.example.domain.usecase.auth.LogoutUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin

class LogoutUiController(
    private val logoutUseCase: LogoutUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            logoutUseCase()
            viewer.view("You have been logged out successfully.\n")
        }
    }
}