package common.di

import domain.usecase.project.DeleteStateFromProjectUseCase
import org.example.domain.usecase.auth.LoginUseCase
import org.example.domain.usecase.auth.LogoutUseCase
import org.example.domain.usecase.auth.CreateUserUseCase
import org.example.domain.usecase.project.*
import org.example.domain.usecase.task.*
import org.koin.dsl.module


val useCasesModule = module {
    single { LogoutUseCase(get()) }
    single { LoginUseCase(get()) }
    single { CreateUserUseCase(get()) }
    single { AddMateToProjectUseCase(get(),get(),get()) }
    single { AddStateToProjectUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { DeleteMateFromProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { DeleteStateFromProjectUseCase(get()) }
    single { EditProjectNameUseCase(get()) }
    single { GetAllTasksOfProjectUseCase(get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { GetTaskHistoryUseCase(get()) }
    single { GetTaskUseCase(get()) }
    single { AddMateToTaskUseCase(get()) }
    single { DeleteMateFromTaskUseCase(get()) }
    single { EditTaskStateUseCase(get()) }
    single { EditTaskTitleUseCase(get(),get(),get()) }
}