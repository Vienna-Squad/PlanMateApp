package org.example.presentation.controller.auth

import org.example.domain.InvalidInputException
import org.example.domain.entity.User

import org.example.domain.usecase.auth.CreateUserUseCase
import org.example.presentation.controller.UiController
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.ItemViewer
import org.example.presentation.utils.viewer.TextViewer
import org.koin.java.KoinJavaComponent.getKoin

class RegisterUiController(
    private val createUserUseCase: CreateUserUseCase = getKoin().get(),
    private val viewer: ItemViewer<String> = TextViewer(),
    private val input: InputReader<String> = StringInputReader()
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("Please enter the username: ")
            val username = input.getInput()
            print("Please enter the password: ")
            val password = input.getInput()
            print("Please enter the role (ADMIN or MATE): ")
            val role = input.getInput().let { value ->
                User.UserRole.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                    ?: throw InvalidInputException("Invalid role: \"$value\". Please enter either ADMIN or MATE.")
            }
            if (username.isBlank() || password.isBlank()) throw InvalidInputException("Username and password cannot be empty.")
            createUserUseCase.invoke(
                username = username,
                password = password,
                role = role
            )
            viewer.view("User \"$username\" has been registered successfully.\n")
        }
    }
}