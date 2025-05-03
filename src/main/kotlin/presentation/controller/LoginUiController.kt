package org.example.presentation.controller

import org.example.domain.NotFoundException
import org.example.domain.entity.UserType
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.auth.LoginUseCase
import org.example.presentation.App
import org.example.presentation.utils.interactor.InputReader
import org.example.presentation.utils.interactor.StringInputReader
import org.example.presentation.utils.viewer.printSwimlanes
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class LoginUiController(
    private val loginUseCase: LoginUseCase=getKoin().get(),
    private val inputReader: InputReader<String> = StringInputReader(),
    private val projectsRepository: ProjectsRepository= getKoin().get(),
    private val tasksRepository: TasksRepository= getKoin().get(),
    private val mateApp: App =  getKoin().get(named("mate")),
    private val adminApp: App = getKoin().get(named("admin")),
) : UiController {
    override fun execute() {
        tryAndShowError {
            print("enter username: ")
            val username = inputReader.getInput()
            print("enter password: ")
            val password = inputReader.getInput()
            if (username.isBlank() || password.isBlank())
                throw NotFoundException("Username or password cannot be empty!")
            val user = loginUseCase(username, password)
            val projects=projectsRepository.getAllProjects().getOrElse {
                throw NotFoundException("No projects found!")
            }
            val tasks=tasksRepository.getAllTasks().getOrElse {
                throw NotFoundException("No projects found!")
            }
           // printSwimlanes(projects,tasks)
            when (user.type) {
                UserType.MATE -> {
                    mateApp.run()
                }
                UserType.ADMIN ->{
                     adminApp.run()
                }
            }
        }
    }
}

