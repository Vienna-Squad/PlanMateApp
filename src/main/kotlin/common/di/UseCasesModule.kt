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
    single { CreateUserUseCase(get(), get()) }
    single { AddMateToProjectUseCase(get(), get()) }
    single { AddStateToProjectUseCase(get(), get()) }
    single { CreateProjectUseCase(get(), get(), get()) }
    single { DeleteMateFromProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { DeleteStateFromProjectUseCase(get(), get()) }
    single { EditProjectNameUseCase(get(), get()) }
    single { GetAllTasksOfProjectUseCase(get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { GetProjectHistoryUseCase(get()) }
    single { DeleteTaskUseCase(get(), get()) }
    single { GetTaskHistoryUseCase(get()) }
    single { GetTaskUseCase(get()) }
    single { AddMateToTaskUseCase(get(), get()) }
    single { DeleteMateFromTaskUseCase(get(), get()) }
    single { EditTaskStateUseCase(get(), get()) }
    single { EditTaskTitleUseCase(get(), get()) }
    single { GetAllProjectsUseCase(get(), get()) }
}