package org.example.presentation.controller.auth

import org.example.domain.NotFoundException
import org.example.domain.entity.UserRole
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.StringViewer
import org.koin.java.KoinJavaComponent.getKoin

class RegisterUiController(
    private val registerUserUseCase: RegisterUserUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = StringViewer(),
    private val inputReader: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Enter UserName : ")
            val username = inputReader.getInput()
            print("Enter password : ")
            val password = inputReader.getInput()
            print("Enter Role (ADMIN) or (MATE) : ")
            val role = inputReader.getInput().let { value ->
                UserRole.entries.firstOrNull { it.name == value } ?: throw NotFoundException("Invalid role: $value")
            }
            if (username.isBlank() || password.isBlank())
                throw NotFoundException("Username or password cannot be empty!")
            registerUserUseCase.invoke(
                username = username,
                password = password,
                role = role
            ).onSuccess {
                viewer.view("user created successfully!!")
            }.exceptionOrNull()
        }
    }


}