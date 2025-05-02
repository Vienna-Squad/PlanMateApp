package org.example.presentation.controller

import org.example.domain.NoFoundException
import org.example.domain.entity.UserType
import org.example.domain.usecase.auth.RegisterUserUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.core.Koin
import org.koin.java.KoinJavaComponent.getKoin

class RegisterUiController(
    private val registerUserUseCase: RegisterUserUseCase = getKoin().get(),
    private val interactor: Interactor<String> = StringInteractor()
): UiController {
    override fun execute() {
        tryAndShowError {
            println("( Create User )")
            print("Enter UserName : ")
            val username = interactor.getInput()
            print("Enter password : ")
            val password = interactor.getInput()
            println("Enter Role : ")
            print("please Enter (ADMIN) or (MATE) : ")
            val role = interactor.getInput()

            if(username.isBlank()||password.isBlank()||role.isBlank())
                throw NoFoundException()

            registerUserUseCase.invoke(
                username = username,
                password = password ,
                role = UserType.entries
                    .firstOrNull{ it.name == role}
                    .also { userType-> if (userType==null) throw NoFoundException() }
                    .let { UserType.valueOf(role) }
            )
        }
    }

}