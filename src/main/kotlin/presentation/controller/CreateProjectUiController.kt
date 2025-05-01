package org.example.presentation.controller

import org.example.domain.InvalidIdException
import org.example.domain.usecase.project.CreateProjectUseCase
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.interactor.StringInteractor
import org.koin.java.KoinJavaComponent.getKoin

class CreateProjectUiController(
    private val createProjectUseCase: CreateProjectUseCase = getKoin().get(),
    private val stringInteractor: Interactor<String> = StringInteractor(),
) : UiController {
    override fun execute() {
        tryAndShowError {
            println("enter name of project: ")
            val name = stringInteractor.getInput()
            if (name.isEmpty()) throw InvalidIdException()

            println("Enter your states separated by commas: ")
            val statesInput = stringInteractor.getInput()
            val states = statesInput.split(",").map { it.trim() }

            println("enter your id: ")
            val creatorId = stringInteractor.getInput()

            println("Enter matesId separated by commas: ")
            val matesIdInput = stringInteractor.getInput()
            val matesId = matesIdInput.split(",").map { it.trim() }

            createProjectUseCase(name = name, states = states, creatorId = creatorId, matesIds = matesId)
            println("Project created successfully")

        }
    }
}
